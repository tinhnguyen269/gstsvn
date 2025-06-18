package com.example.serviceapp.customer.companyInfor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyInforController {
    @GetMapping("/companyInfor_doInit")
    public String doInit() {
        return "customer/companyInfor";
    }
}
