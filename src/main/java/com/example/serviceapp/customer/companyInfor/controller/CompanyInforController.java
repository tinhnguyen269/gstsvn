package com.example.serviceapp.customer.companyInfor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyInforController {
    @GetMapping("/company-info")
    public String doInit() {
        return "customer/company-info/company_info";
    }
}
