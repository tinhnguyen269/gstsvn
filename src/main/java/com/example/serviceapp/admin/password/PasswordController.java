package com.example.serviceapp.admin.password;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/password")
public class PasswordController {
    @GetMapping("")
    public String showPasswordPage() {
        return "admin/authenticate/password";
    }
}
