
package com.example.serviceapp.customer.home.controller;

import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.common.entity.Feedback;
import com.example.serviceapp.common.entity.Post;
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
    public String index(Model model) {
        List<Post> Post = homeService.findAll();
        List<Feedback> feedbacks = homeService.findAllFeedback();
        model.addAttribute("Feedback", feedbacks);
        model.addAttribute("Post", Post);
        model.addAttribute("customer" , new Customer());
        return "customer/index";
    }

}
