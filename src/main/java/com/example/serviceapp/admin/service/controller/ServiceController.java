
package com.example.serviceapp.admin.service.controller;

import com.example.serviceapp.admin.service.ServiceKey;
import com.example.serviceapp.admin.service.service.ServiceService;
import com.example.serviceapp.common.entity.Services;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/service_doInit")
    public String doInit(Model model) {
        return "/admin/service/Service";
    }

    @GetMapping("/service")
    @ResponseBody
    public List<Services> getAllServices() {
        return serviceService.getAllServices();
    }

    @GetMapping("/service/{id}")
    @ResponseBody
    public Optional<Services> getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id);
    }

    @PostMapping("/service")
    @ResponseBody
    public Services createService(@RequestBody Services services) {
        return serviceService.createService(services);
    }

    @PutMapping("/service/{id}")
    @ResponseBody
    public Services updateService(@PathVariable Long id, @RequestBody Services services) {
        return serviceService.updateService(id, services);
    }

    @DeleteMapping("/service/{id}")
    @ResponseBody
    public void deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
    }
}
