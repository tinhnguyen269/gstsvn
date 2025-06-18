
package com.example.serviceapp.customer.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    @GetMapping("/home_doInit")
    public String index() {
        return "customer/index";
    }

}
