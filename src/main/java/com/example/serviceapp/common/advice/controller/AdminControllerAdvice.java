package com.example.serviceapp.common.advice.controller;

import com.example.serviceapp.common.advice.service.AdviceService;
import com.example.serviceapp.common.entity.Services;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(basePackages = "com.example.serviceapp.admin")
public class AdminControllerAdvice {

    private final AdviceService adviceService;


    public AdminControllerAdvice(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    @ModelAttribute("serviceAdvice")
    public List<Services> services() {
        List<Services> services = adviceService.findAll();
        return services;
    }
}
