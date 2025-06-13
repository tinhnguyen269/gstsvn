
package com.example.serviceapp.customer.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/customer")
public class HomeController {

    @GetMapping("/home")
    public String index() {
        return "customer/index";
    }

    @PostMapping("/{id}/doTransition")
    public String doTransition(@PathVariable int id) {
        return "customer/shop-detail";
    }

    @GetMapping("/doCompanyInfor")
    public String doCompanyInfor() {
        return "redirect: customer/inforCompany";
    }
}
