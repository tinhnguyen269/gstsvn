package com.example.serviceapp.customer.price.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PriceListController {

    @GetMapping("/price-list")
    public String doInit(){
        return "customer/price/price-list";
    }
}
