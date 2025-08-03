package com.example.serviceapp.admin.login.controller;

import com.example.serviceapp.admin.login.service.ADLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/login")
public class ADLoginController {
    private final ADLoginService adLoginService;

    public ADLoginController(ADLoginService adLoginService) {
        this.adLoginService = adLoginService;
    }

    @GetMapping("")
    public String doLogin(Model model) {
        return "admin/authenticate/login";
    }

}
