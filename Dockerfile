# Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Gradle Wrapper và các file cấu hình trước
COPY gradlew /app/
COPY gradle /app/gradle/
COPY build.gradle /app/

# Copy source code
COPY src /app/src
COPY src/main/resources /app/src/main/resources


# Set quyền cho gradlew
RUN chmod +x ./gradlew

# Build project
RUN ./gradlew clean build --no-daemon

# Run stage
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy jar file từ build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Mở cổng 8080
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
