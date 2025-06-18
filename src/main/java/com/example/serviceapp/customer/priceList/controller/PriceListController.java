package com.example.serviceapp.customer.priceList.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PriceListController {

    @GetMapping("/priceList_doInit")
    public String doInit(){
        return "customer/price-list";
    }
}
