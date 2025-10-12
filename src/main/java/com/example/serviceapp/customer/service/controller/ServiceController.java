package com.example.serviceapp.customer.service.controller;

import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.service.service.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dich-vu")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/{slug}")
    public String viewPost(@PathVariable String slug, Model model) {
        Services services = serviceService.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post slug: " + slug));
        model.addAttribute("services", services);

        return "customer/service/service_detail";
    }

}
