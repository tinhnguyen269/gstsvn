package com.example.serviceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
public class ServiceAppApplication {
    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            System.setProperty("MYSQL_URL", dotenv.get("MYSQL_URL", System.getenv("MYSQL_URL")));
            System.setProperty("MYSQL_USER", dotenv.get("MYSQL_USER", System.getenv("MYSQL_USER")));
            System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD", System.getenv("MYSQL_PASSWORD")));
            System.setProperty("CLOUDINARY_CLOUD_NAME", dotenv.get("CLOUDINARY_CLOUD_NAME", System.getenv("CLOUDINARY_CLOUD_NAME")));
            System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY", System.getenv("CLOUDINARY_API_KEY")));
            System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET", System.getenv("CLOUDINARY_API_SECRET")));
            System.setProperty("SPRING_MAIL_USERNAME", dotenv.get("SPRING_MAIL_USERNAME", System.getenv("SPRING_MAIL_USERNAME")));
            System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD", System.getenv("SPRING_MAIL_PASSWORD")));
            System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT", System.getenv("SERVER_PORT")));
        } catch (Exception e) {
            System.out.println("⚠️ Warning: .env file not found, using system environment variables instead.");
        }

        SpringApplication.run(ServiceAppApplication.class, args);
    }
}

