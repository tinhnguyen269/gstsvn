package com.example.serviceapp.admin.authenticate.service;

import com.example.serviceapp.admin.authenticate.repository.RoleRepository;
import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.common.entity.Employee;
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

        // Thiết lập liên kết 2 chiều
        Employee employee = user.getEmployee();
        if (employee != null) {
            employee.setUser(user);
        }

        userRepo.save(user);

        sendActivationEmail(user.getEmail(), activationCode);
    }

    private void sendActivationEmail(String to, String code) {
        String subject = "Kích hoạt tài khoản của bạn";
        String link = appHost + "/activate?code=" + code;

        String content = """
                <p>Chào bạn,</p>
                <p>Vui lòng nhấn vào liên kết sau để kích hoạt tài khoản:</p>
                <p><a href="%s">Kích hoạt ngay</a></p>
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
            <p>Bạn đã yêu cầu khôi phục mật khẩu.</p>
            <p>Nhấn vào liên kết dưới đây để đặt lại mật khẩu:</p>
            <p><a href="%s">Đặt lại mật khẩu</a></p>
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

