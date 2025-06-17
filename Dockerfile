# 1단계: Build
FROM gradle:8.12.1-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon || true
COPY src ./src
RUN gradle bootJar --no-daemon

# 2단계: Runtime
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-jar", "app.jar"]