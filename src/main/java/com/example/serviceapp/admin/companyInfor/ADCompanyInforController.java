package com.example.serviceapp.admin.companyInfor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ADCompanyInforController {
@GetMapping("/company-infor")
    public String doInit() {
        return "admin/company-infor";
    }
}
