package com.example.serviceapp.customer.home.controller;

import com.example.serviceapp.customer.home.service.HomeService;
import com.example.serviceapp.util.DateFormatter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("Post", homeService.findPost9());
        model.addAttribute("Feedback", homeService.findProjectActual10());
        model.addAttribute("Project", homeService.findProject());
        model.addAttribute("customer", new com.example.serviceapp.common.entity.Customer());
        model.addAttribute("dateFormatter", new DateFormatter());
        return "customer/index";
    }
}
