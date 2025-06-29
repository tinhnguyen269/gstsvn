package com.example.serviceapp.admin.register.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {
    @GetMapping("/register")
    public String doRegister() {
        return "admin/authenticate/register";
    }
}
