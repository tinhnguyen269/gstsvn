package com.example.serviceapp.admin.authenticate.service;

import com.example.serviceapp.admin.authenticate.repository.RoleRepository;
import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.common.entity.Role;
import com.example.serviceapp.common.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final JavaMailSender mailSender;
    @Value("${app.host}")
    private String appHost;

    public UserService(UserRepository userRepo, RoleRepository roleRepo, JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mailSender = mailSender;
    }

    public void registerUser(User user) {
        // Mã hóa password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setEnabled(false);

        // Gán role mặc định
        Role role = roleRepo.findByRoleName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Set.of(role));

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
            user.setActivationCode(null); // không dùng lại được
            userRepo.save(user);
            return true;
        }
        return false;
    }
}

