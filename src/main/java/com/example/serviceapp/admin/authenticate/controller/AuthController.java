package com.example.serviceapp.admin.authenticate.controller;

import com.example.serviceapp.admin.authenticate.dto.ForgotPasswordDTO;
import com.example.serviceapp.admin.authenticate.dto.ResetPasswordDTO;
import com.example.serviceapp.admin.authenticate.service.UserService;
import com.example.serviceapp.common.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "admin/authenticate/register";
    }


    @PostMapping("/register")
    public String registerUser(@Validated(User.OnCreate.class) @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        // Kiểm tra lỗi validate
        if (result.hasErrors()) {
            return "admin/authenticate/register";
        }

        // Kiểm tra xác nhận mật khẩu
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            model.addAttribute("passwordError", "Mật khẩu xác nhận không khớp!");
            return "admin/authenticate/register";
        }

        userService.registerUser(user);
        model.addAttribute("message", "Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt.");
        return "admin/authenticate/login";
    }


    @GetMapping("/activate")
    public String activate(@RequestParam("code") String code, Model model) {
        boolean activated = userService.activateUser(code);
        model.addAttribute("message", activated ? "Kích hoạt thành công!" : "Mã không hợp lệ hoặc đã được sử dụng.");
        return "admin/authenticate/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/authenticate/login";
    }


    @GetMapping("/access-denied")
    public String accessDenied() {
        return "admin/error/access-denied";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/authenticate/forgot-password";
    }
    @PostMapping("/forgot-password")
    public String processForgotPassword(@Validated @ModelAttribute("user") ForgotPasswordDTO user,
                                        BindingResult bindingResult, Model model) {

        // Kiểm tra lỗi validate
        if (bindingResult.hasErrors()) {
            return "admin/authenticate/forgot-password";
        }
        Optional<User> userOpt = userService.findByEmail(user.getEmail());
        if (userOpt.isPresent()) {
            userService.sendResetPasswordToken(userOpt.get());
            model.addAttribute("message", "Đã gửi email đặt lại mật khẩu, vui lòng kiểm tra.");
        } else {
            model.addAttribute("message", "Không tìm thấy email trong hệ thống.");
        }
        return "admin/authenticate/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<User> userOpt = userService.findByResetToken(token);
        if (userOpt.isEmpty()) {
            model.addAttribute("message", "Liên kết không hợp lệ hoặc đã hết hạn.");
            return "admin/authenticate/login";
        }
        model.addAttribute("token", token);
        model.addAttribute("user", new ResetPasswordDTO());
        return "admin/authenticate/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam("token") String token,
            @Validated @ModelAttribute("user") ResetPasswordDTO user
           ,BindingResult bindingResult,Model model) {

        // Kiểm tra lỗi validate
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("token", token);
            return "admin/authenticate/reset-password";
        }
        // Kiểm tra xác nhận mật khẩu
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "error.passwordConfirmation", "Mật khẩu xác nhận không khớp!");
            model.addAttribute("user", user);
            model.addAttribute("token", token);
            return "admin/authenticate/reset-password";
        }

        boolean success = userService.resetPassword(token, user.getPassword());

        model.addAttribute("message", success
                ? "Đặt lại mật khẩu thành công!"
                : "Token không hợp lệ hoặc đã hết hạn.");

        return "admin/authenticate/login";
    }


}

