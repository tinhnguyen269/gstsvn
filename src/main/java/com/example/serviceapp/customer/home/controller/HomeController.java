
package com.example.serviceapp.customer.home.controller;

import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.home.service.HomeService;
import com.example.serviceapp.customer.service.service.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {
    private final HomeService homeService;

    public HomeController( HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home_doInit")
    public String index(Model model) {
        List<Services> services = homeService.findAll();
        model.addAttribute("services", services);
        return "customer/index";
    }
}
