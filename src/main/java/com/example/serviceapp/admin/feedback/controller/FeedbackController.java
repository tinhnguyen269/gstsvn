package com.example.serviceapp.admin.feedback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class FeedbackController {
    @GetMapping("/feedback")
    public String doInit() {
        return "admin/feedback/feedback";
    }
}
