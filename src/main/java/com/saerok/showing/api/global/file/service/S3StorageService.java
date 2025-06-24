package com.saerok.showing.api.global.file.service;

import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import com.saerok.showing.api.global.file.dto.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    private final S3Client s3Client;

    @Value("${BUCKET_NAME}")
    private String bucketName;

    @Value("${S3_REGION}")
    private String region;

    private static final List<String> ALLOWED_EXTENSIONS =
        List.of(".jpg", ".jpeg", ".png", ".gif", ".pdf");

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public FileUploadResult uploadFile(MultipartFile file, String folder) {
        validateFileSize(file);
        String originalFilename = file.getOriginalFilename();
        String fileExtension = extractFileExtension(originalFilename);
        validateExtension(fileExtension);

        String storedFileName = checkFolder(folder) + UUID.randomUUID() + fileExtension;
        return uploadS3(file, storedFileName, originalFilename);
    }

    private FileUploadResult uploadS3(MultipartFile file, String storedFileName, String originalFilename) {
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(storedFileName)
            .acl(ObjectCannedACL.BUCKET_OWNER_FULL_CONTROL)
            .contentType(file.getContentType())
            .contentDisposition("inline")
            .build();

        try {
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw ShowingException.from(ErrorCode.S3_UPLOAD_FAILURE);
        }

        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, storedFileName);

        return FileUploadResult.builder()
            .storedFileName(storedFileName)
            .fileUrl(fileUrl)
            .build();
    }

    private String extractFileExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw ShowingException.from(ErrorCode.UNSUPPORTED_FILE_EXTENSION);
        }
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw ShowingException.from(ErrorCode.FILE_SIZE_EXCEEDED);
        }
    }

    private void validateExtension(String extension) {
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw ShowingException.from(ErrorCode.UNSUPPORTED_FILE_EXTENSION);
        }
    }

    private String checkFolder(String folder) {
        return folder.endsWith("/") ? folder : folder + "/";
    }
}
