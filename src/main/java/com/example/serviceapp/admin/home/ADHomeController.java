package com.example.serviceapp.admin.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ADHomeController {

    @GetMapping("/home")
    public String doInit(Model model) {
        return "/admin/index";
    }
}
