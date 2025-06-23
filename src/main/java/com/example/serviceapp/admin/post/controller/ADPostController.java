package com.example.serviceapp.admin.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ADPostController {
    @GetMapping("/post")
    public String doInit() {
        return "admin/post";
    }
}
