package com.example.serviceapp.customer.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectCompleteController {
    @GetMapping("/project-complete")
    public String projectComplete_doInit() {
        return "customer/project/project_complete";
    }
}
