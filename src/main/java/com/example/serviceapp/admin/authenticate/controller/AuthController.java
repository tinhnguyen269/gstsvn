package com.example.serviceapp.admin.authenticate.controller;

import com.example.serviceapp.admin.authenticate.service.UserService;
import com.example.serviceapp.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/authenticate/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
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
}

