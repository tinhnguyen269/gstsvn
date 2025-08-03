package com.example.serviceapp.admin.register.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/register")
public class RegisterController {
    @GetMapping("")
    public String doRegister() {
        return "admin/authenticate/register";
    }
}
