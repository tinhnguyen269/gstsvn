package com.example.serviceapp.customer.projectComplete.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectCompleteController {
    @GetMapping("/projectComplete_doInit")
    public String projectComplete_doInit() {
        return "customer/projectComplete";
    }
}
