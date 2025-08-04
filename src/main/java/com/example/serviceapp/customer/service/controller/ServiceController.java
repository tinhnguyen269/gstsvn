package com.example.serviceapp.customer.service.controller;

import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.service.service.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/services/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Services services = serviceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
        model.addAttribute("services", services);

        return "customer/service/service_detail";
    }

}
