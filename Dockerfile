# 1단계: Build
FROM gradle:8.12.1-jdk21 AS build
WORKDIR /app

# Gradle 설정 및 종속성 캐시 최적화
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon || true

# 애플리케이션 소스 복사 및 빌드
COPY src ./src
RUN gradle bootJar --no-daemon

# 2단계: Runtime
FROM openjdk:21-jdk-slim
WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Dserver.port=8080", "-jar", "app.jar"]
