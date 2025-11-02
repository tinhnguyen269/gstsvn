package com.example.serviceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
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
            System.setProperty("SENDGRID_API_KEY", dotenv.get("SENDGRID_API_KEY", System.getenv("SENDGRID_API_KEY")));
            System.setProperty("REDIS_URL", dotenv.get("REDIS_URL", System.getenv("REDIS_URL")));
            System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT", System.getenv("SERVER_PORT")));
        } catch (Exception e) {
            System.out.println("⚠️ Warning: .env file not found, using system environment variables instead.");
        }

        SpringApplication.run(ServiceAppApplication.class, args);
    }
}

