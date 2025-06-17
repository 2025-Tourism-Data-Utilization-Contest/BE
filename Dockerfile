# 1단계: Build
FROM gradle:8.12.1-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon || true  # 종속성 캐싱
COPY src ./src
RUN gradle bootJar --no-daemon

# 2단계: Runtime
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# 환경변수 기본값 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-Dserver.port=${SERVER_PORT}", "-jar", "app.jar"]