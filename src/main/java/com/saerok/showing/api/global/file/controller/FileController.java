package com.saerok.showing.api.global.file.controller;

import com.saerok.showing.api.global.file.dto.FileUploadResponse;
import com.saerok.showing.api.global.file.service.FileService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Tag(name = "File", description = "파일 관리")
public class FileController {

    private final FileService fileService;

    @Operation(
        summary = "탐조 사진 업로드",
        description = """
            [MEMBER 이상 가능] 탐조하며 찍은 사진을 업로드합니다.<br>
            한장만 업로드 가능하며 5MB를 넘길 수 없습니다.<br>
            가능한 확장자는 ".jpg", ".jpeg", ".png", ".gif", ".pdf"입니다.
            """)
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping(
        value = "/bird",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<FileUploadResponse> uploadBirdImage(
        @RequestParam(name = "multipartFile") MultipartFile multipartFile
    ) {
        FileUploadResponse fileUploadResponse = fileService.uploadFile(multipartFile, "bird/");
        return ApiResponse.success(fileUploadResponse);
    }

    @Operation(
        summary = "장소 사진 업로드",
        description = """
            [ADMIN 이상 가능] 장소(HOTEL, THEME) 사진을 업로드합니다.<br>
            여러 장 업로드 가능하며 각 파일은 5MB를 넘길 수 없습니다.<br>
            가능한 확장자는 ".jpg", ".jpeg", ".png", ".gif", ".pdf"입니다.
            """)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
        value = "/place",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<List<FileUploadResponse>> uploadPlaceImage(
        @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles
    ) {
        List<FileUploadResponse> fileUploadResponse = fileService.uploadFiles(multipartFiles, "place/");
        return ApiResponse.success(fileUploadResponse);
    }

    @Operation(
        summary = "리뷰 사진 업로드",
        description = """
            [MEMBER 이상 가능] 리뷰 사진을 업로드합니다.<br>
            여러 장 업로드 가능하며 각 파일은 5MB를 넘길 수 없습니다.<br>
            가능한 확장자는 ".jpg", ".jpeg", ".png", ".gif", ".pdf"입니다.
            """)
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping(
        value = "/review",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<List<FileUploadResponse>> uploadReviewImage(
        @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles
    ) {
        List<FileUploadResponse> fileUploadResponses = fileService.uploadFiles(multipartFiles, "review/");
        return ApiResponse.success(fileUploadResponses);
    }

    @Operation(
        summary = "프로필 사진 업로드",
        description = """
            [MEMBER 이상 가능] 탐조하며 찍은 사진을 업로드합니다.<br>
            여러 장 업로드 가능하며 각 파일은 5MB를 넘길 수 없습니다.<br>
            가능한 확장자는 ".jpg", ".jpeg", ".png", ".gif", ".pdf"입니다.
            """)
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping(
        value = "/profile",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<List<FileUploadResponse>> uploadProfileImage(
        @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles
    ) {
        List<FileUploadResponse> fileUploadResponses = fileService.uploadFiles(multipartFiles, "profile/");
        return ApiResponse.success(fileUploadResponses);
    }
}
