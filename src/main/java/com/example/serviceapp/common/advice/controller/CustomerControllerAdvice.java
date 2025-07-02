package com.example.serviceapp.common.advice.controller;

import com.example.serviceapp.common.advice.service.AdviceService;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(basePackages = "com.example.serviceapp.customer")
public class CustomerControllerAdvice {

    private final AdviceService adviceService;

    public CustomerControllerAdvice(AdviceService adviceService) {
        this.adviceService = adviceService;
    }
    @ModelAttribute("currentRequestURI")
    public String currentRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("postAdvice")
    public List<Post> latestPosts() {
        return adviceService.getTop10NewestPosts();
    }

    @ModelAttribute("serviceAdvice")
    public List<Services> services() {
        List<Services> services = adviceService.findAll();
        return services;
    }
}
