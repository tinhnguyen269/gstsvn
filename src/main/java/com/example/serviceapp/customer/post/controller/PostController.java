package com.example.serviceapp.customer.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    @GetMapping("/post_doInit")
    public String doInit(){
        return "customer/post";
    }
}
