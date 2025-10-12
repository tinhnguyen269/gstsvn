package com.example.serviceapp.customer.price.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bang-gia-dich-vu")
public class PriceListController {

    @GetMapping("")
    public String doInit(){
        return "customer/price/price-list";
    }
}
