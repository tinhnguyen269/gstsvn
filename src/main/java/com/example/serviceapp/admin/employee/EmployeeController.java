package com.example.serviceapp.admin.employee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EmployeeController {
    @GetMapping("/employee")
    public String doInit() {
        return "admin/employee/employee";
    }
}
