package com.example.serviceapp.admin.login.controller;

import com.example.serviceapp.admin.login.service.ADLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ADLoginController {
    private final ADLoginService adLoginService;

    public ADLoginController(ADLoginService adLoginService) {
        this.adLoginService = adLoginService;
    }

    @GetMapping("/login")
    public String doLogin(Model model) {
        return "admin/authentication-login";
    }

}
