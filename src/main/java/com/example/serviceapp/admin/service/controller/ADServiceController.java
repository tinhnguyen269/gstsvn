
package com.example.serviceapp.admin.service.controller;

import com.example.serviceapp.admin.service.service.ADServiceService;
import com.example.serviceapp.common.entity.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;



@Controller
@RequestMapping("/admin")
public class ADServiceController {

    private final ADServiceService serviceService;

    public ADServiceController(ADServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/service/add")
    public String showAddForm(Model model) {
        model.addAttribute("service", new Services());
        return "admin/service/add_service";
    }

    @PostMapping("/service/save")
    public String saveService(@ModelAttribute Services services) {
        services.setCreateAt(LocalDateTime.now());
        serviceService.save(services);
        return "redirect:/admin/service/list";
    }

    @GetMapping("/service/list")
    public String listService(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false, defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Services> servicePage;

        if (keyword != null && !keyword.isEmpty()) {
            servicePage = serviceService.searchServices(keyword, pageable);
        } else {
            servicePage = serviceService.findAll(pageable);
        }

        model.addAttribute("servicePage", servicePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);

        return "admin/service/service";
    }

    @GetMapping("")
    public String redirectToList() {
        return "redirect:/admin/service/list";
    }

    @GetMapping("/service/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Services service = serviceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + id));
        model.addAttribute("service", service);
        return "admin/service/edit_service";
    }

    @PostMapping("/service/update/{id}")
    public String updateService(@PathVariable Long id, @ModelAttribute Services service) {
        Services existingService = serviceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + id));

        existingService.setServiceName(service.getServiceName());
        existingService.setContent(service.getContent());
        existingService.setIcon(service.getIcon());
        existingService.setImgPrice(service.getImgPrice());
        existingService.setUpdateAt(LocalDateTime.now());

        serviceService.save(existingService);
        return "redirect:/admin/service/list";
    }
    @PostMapping("/service/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        Services existingService = serviceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + id));

        existingService.setDeleteFlag(1);
        existingService.setUpdateAt(LocalDateTime.now());
        serviceService.save(existingService);

        return "redirect:/admin/service/list";
    }
}
