package com.example.serviceapp.common.advice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.example.serviceapp.admin")
public class AdminControllerAdvice {
    @ModelAttribute
    public void addCurrentURI(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
    }
}