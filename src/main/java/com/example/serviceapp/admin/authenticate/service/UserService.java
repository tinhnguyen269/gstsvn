package com.example.serviceapp.admin.authenticate.service;

import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.common.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.host}")
    private String appHost;

    public UserService(UserRepository userRepo, JavaMailSender mailSender, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        // Mã hóa password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);

        // Sinh mã xác thực
        String activationCode = UUID.randomUUID().toString();
        user.setActivationCode(activationCode);

        userRepo.save(user);

        sendActivationEmail(user.getEmail(), activationCode);
    }

    private void sendActivationEmail(String to, String code) {
        String subject = "Kích hoạt tài khoản của bạn";
        String link = appHost + "/activate?code=" + code;

        String content = """
        <html>
            <body style="font-family: Arial, sans-serif; font-size: 14px; color: #333;">
                <p>Xin chào,</p>
                
                <p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>Service App</strong>.</p>
                
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
                
                <p>Trân trọng,<br/>Đội ngũ hỗ trợ Service App</p>
            </body>
        </html>
        """.formatted(link);


        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi mail thất bại", e);
        }
    }

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

    public void sendResetPasswordToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepo.save(user);

        String subject = "Khôi phục mật khẩu";
        String resetLink = appHost + "/reset-password?token=" + token;

        String content = """
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

                <p>Trân trọng,<br/>Đội ngũ hỗ trợ Service App</p>
            </body>
        </html>
        """.formatted(resetLink);


        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không gửi được mail khôi phục mật khẩu", e);
        }
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

}

