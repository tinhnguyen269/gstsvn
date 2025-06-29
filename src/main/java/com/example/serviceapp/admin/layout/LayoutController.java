package com.example.serviceapp.admin.layout;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class LayoutController {
    @RequestMapping("/layout-light")
    public String showlayoutLight() {
        return "admin/layout/layout-sidenav-light";
    }

    @GetMapping("/layout-dark")
    public String showlayoutDark() {
        return "admin/layout/layout-static";
    }
}
