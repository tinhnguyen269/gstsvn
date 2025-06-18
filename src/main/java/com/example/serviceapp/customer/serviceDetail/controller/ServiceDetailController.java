package com.example.serviceapp.customer.serviceDetail.controller;

import com.example.serviceapp.customer.serviceDetail.service.ServiceDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceDetailController {

    private final ServiceDetailService serviceService;

    public ServiceDetailController(ServiceDetailService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/serviceDetail_doInit")
    public String doDetail() {
        return "customer/shop-detail";
    }
}
