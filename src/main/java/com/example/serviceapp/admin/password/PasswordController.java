package com.example.serviceapp.admin.password;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PasswordController {
    @GetMapping("/password")
    public String showPasswordPage() {
        return "admin/authenticate/password";
    }
}
