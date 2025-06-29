package com.example.serviceapp.admin.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CustomerController {
@GetMapping("/customer")
    public String doInit() {
        return "admin/customer/customer";
    }
}
