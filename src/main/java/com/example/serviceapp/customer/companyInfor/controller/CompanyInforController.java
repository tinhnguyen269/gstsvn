package com.example.serviceapp.customer.companyInfor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gioi-thieu")
public class CompanyInforController {
    @GetMapping("")
    public String doInit() {
        return "customer/company-info/company_info";
    }
}
