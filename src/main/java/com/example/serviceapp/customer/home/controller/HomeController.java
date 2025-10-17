
package com.example.serviceapp.customer.home.controller;

import com.example.serviceapp.common.entity.*;
import com.example.serviceapp.customer.home.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/")
public class HomeController {
    private final HomeService homeService;

    public HomeController( HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("")
    public String index(Model model) {
        List<Post> Post = homeService.findPost9();
        List<Feedback> feedbacks = homeService.findFeedback10();
        List<Project> projects = homeService.findProject9();
        model.addAttribute("Feedback", feedbacks);
        model.addAttribute("Post", Post);
        model.addAttribute("customer" , new Customer());
        model.addAttribute("Project", projects);
        return "customer/index";
    }

    @GetMapping("/home")
    public String home(){
        return "redirect:/";
    }

}
