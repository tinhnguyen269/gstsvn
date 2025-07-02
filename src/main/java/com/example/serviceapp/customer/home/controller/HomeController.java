
package com.example.serviceapp.customer.home.controller;

import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.home.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;

    public HomeController( HomeService homeService) {
        this.homeService = homeService;
    }


    @GetMapping("")
    public String index() {
        return "customer/index";
    }

}
