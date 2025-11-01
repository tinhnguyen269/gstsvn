package com.example.serviceapp.admin.authenticate.service;

import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.common.entity.User;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.host}")
    private String appHost;

    @Value("${company.email}")
    private String companyEmail;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ============= Đăng ký tài khoản =============
    @Async
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);

        String activationCode = UUID.randomUUID().toString();
        user.setActivationCode(activationCode);

        userRepo.save(user);

        sendActivationEmail(user.getEmail(), activationCode);
    }

    private void sendActivationEmail(String to, String code) {
        String subject = "Kích hoạt tài khoản của bạn";
        String link = appHost + "/activate?code=" + code;

        String contentHtml = """
        <html>
            <body style="font-family: Arial, sans-serif; font-size: 14px; color: #333;">
                <p>Xin chào,</p>
                <p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>GSTS</strong>.</p>
                <p>Vui lòng nhấn vào nút bên dưới để kích hoạt tài khoản của bạn:</p>
                <p>
                    <a href="%s" style="
                        display: inline-block;
                        padding: 10px 20px;
                        background-color: #007bff;
                        color: white;
                        text-decoration: none;
                        border-radius: 5px;
                    ">Kích hoạt tài khoản</a>
                </p>
                <p>Nếu bạn không yêu cầu tạo tài khoản, vui lòng bỏ qua email này.</p>
                <p>Trân trọng,<br/>Đội ngũ hỗ trợ GSTS</p>
            </body>
        </html>
        """.formatted(link);

        sendEmail(to, subject, contentHtml);
    }

    // ============= Gửi token reset password =============
    @Async
    public void sendResetPasswordToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepo.save(user);

        String subject = "Khôi phục mật khẩu";
        String resetLink = appHost + "/reset-password?token=" + token;

        String contentHtml = """
        <html>
            <body style="font-family: Arial, sans-serif; font-size: 14px; color: #333; line-height: 1.6;">
                <p>Xin chào,</p>
                <p>Bạn đã yêu cầu khôi phục mật khẩu cho tài khoản của mình.</p>
                <p>Vui lòng nhấn vào nút bên dưới để đặt lại mật khẩu:</p>
                <p>
                    <a href="%s" style="
                        display: inline-block;
                        padding: 10px 20px;
                        background-color: #28a745;
                        color: white;
                        text-decoration: none;
                        border-radius: 5px;
                        font-weight: bold;
                    ">Đặt lại mật khẩu</a>
                </p>
                <p>Nếu bạn không yêu cầu thao tác này, vui lòng bỏ qua email này.</p>
                <p>Trân trọng,<br/>Đội ngũ hỗ trợ GSTS</p>
            </body>
        </html>
        """.formatted(resetLink);

        sendEmail(user.getEmail(), subject, contentHtml);
    }

    // ============= Gửi email qua SendGrid API =============
    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            Email from = new Email(companyEmail, "GSTS Support");
            Email recipient = new Email(to);
            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, subject, recipient, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("✅ Email đã gửi thành công đến: " + to);
            } else {
                System.err.println("⚠️ Gửi email thất bại. Mã phản hồi: " + response.getStatusCode());
                System.err.println(response.getBody());
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi gửi email: " + e.getMessage());
        }
    }

    // ============= Các nghiệp vụ khác =============
    public boolean activateUser(String code) {
        Optional<User> userOpt = userRepo.findByActivationCode(code);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEnabled(true);
            user.setActivationCode(null);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<User> findByResetToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepo.findByResetPasswordToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
