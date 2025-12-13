package com.example.serviceapp.customer.service.controller;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.home.service.HomeService;
import com.example.serviceapp.customer.service.service.ServiceService;
import com.example.serviceapp.util.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dich-vu")
public class ServiceController {

    private final ServiceService serviceService;
    private final HomeService homeService;

    public ServiceController(ServiceService serviceService, HomeService homeService) {
        this.serviceService = serviceService;
        this.homeService = homeService;
    }

    @GetMapping("/{slug}")
    public String viewService(@PathVariable String slug, Model model) {
        Services services = serviceService.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post slug: " + slug));
        model.addAttribute("services", services);
        List<Post> Post = homeService.findPost9();
        model.addAttribute("Post", Post);
        model.addAttribute("dateFormatter", new DateFormatter());

        return "customer/service/service_detail";
    }

}
