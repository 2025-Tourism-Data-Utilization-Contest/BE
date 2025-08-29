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

# 기본값 (없으면 local로 실행되도록)
ENV SERVER_PORT=8080
ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE ${SERVER_PORT}

# 환경변수를 동적으로 받아 실행
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dserver.port=${SERVER_PORT} -jar app.jar"]
