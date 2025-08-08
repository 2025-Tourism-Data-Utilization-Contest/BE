package com.saerok.showing.api.global.config;

import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class MockDataInitializer {

    private final DataSource dataSource;

    @Value("${cloud.aws.s3.base-url}")
    private String s3BaseUrl;

    private static final String[] SCRIPT_PATHS = {
        "data/01-member.sql",
        "data/02-bird.sql",
        "data/03-theme.sql",
        "data/04-post.sql"
    };

    @PostConstruct
    public void initMockData() {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            for (String path : SCRIPT_PATHS) {
                log.info("📥 SQL 스크립트 로딩 중: {}", path);
                String rawSql = readAndReplaceVariables(path);

                try {
                    conn.setAutoCommit(false);
                    for (String query : rawSql.split(";")) {
                        String trimmed = query.trim();
                        if (!trimmed.isEmpty()) {
                            stmt.execute(trimmed);
                        }
                    }
                    conn.commit();
                    log.info("✅ {} 실행 완료", path);
                } catch (Exception e) {
                    conn.rollback();
                    log.error("❌ {} 실행 중 오류 발생: {}", path, e.getMessage());
                    throw e;
                }
            }
            log.info("🌱 모든 목업 데이터 스크립트가 성공적으로 로딩되었습니다.");
        } catch (Exception e) {
            log.error("🔥 목업 데이터 초기화 실패", e);
            throw ShowingException.from(ErrorCode.MOCK_INIT_FAIL);
        }
    }

    private String readAndReplaceVariables(String path) throws Exception {
        Resource resource = new ClassPathResource(path);
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            return reader.lines()
                .collect(Collectors.joining("\n"))
                .replace("${S3_BASE_URL}", s3BaseUrl);
        }
    }
}
