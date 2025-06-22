package com.example.serviceapp.admin.theme;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ThemeController {
    @RequestMapping("/layout-light")
    public String showlayoutLight() {
        return "admin/layout-sidenav-light";
    }

    @GetMapping("/layout-dark")
    public String showlayoutDark() {
        return "admin/layout-static";
    }
}
