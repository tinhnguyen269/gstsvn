package com.example.serviceapp.admin.service.controller;

import com.example.serviceapp.admin.service.service.ADServiceService;
import com.example.serviceapp.common.entity.Services;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/service")
public class ADServiceController {

    private final ADServiceService serviceService;

    public ADServiceController(ADServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/list")
    public String listServices(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Services> servicePage = keyword.isEmpty()
                ? serviceService.findAll(pageable)
                : serviceService.searchServices(keyword, pageable);

        model.addAttribute("servicePage", servicePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);

        return "admin/service/service";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("service", new Services());
        return "admin/service/add_service";
    }

    @PostMapping("/save")
    public String saveService(@ModelAttribute Services service) {
        service.setCreateAt(LocalDateTime.now());
        serviceService.save(service);
        return "redirect:/admin/service/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("service", getServiceById(id));
        return "admin/service/edit_service";
    }

    @PostMapping("/update/{id}")
    public String updateService(@PathVariable Long id, @ModelAttribute Services updated) {
        Services service = getServiceById(id);
        service.setServiceName(updated.getServiceName());
        service.setContent(updated.getContent());
        service.setIcon(updated.getIcon());
        service.setImgPrice(updated.getImgPrice());
        service.setSlug(updated.getSlug());
        service.setUpdateAt(LocalDateTime.now());
        serviceService.save(service);
        return "redirect:/admin/service/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        Services service = getServiceById(id);
        service.setDeleteFlag(1);
        service.setUpdateAt(LocalDateTime.now());
        serviceService.save(service);
        return "redirect:/admin/service/list";
    }

    @PostMapping("/delSelected")
    public String deleteSelected(@RequestParam("ids") List<Long> ids) {
        serviceService.softDeleteServices(ids);
        return "redirect:/admin/service/list";
    }

    private Services getServiceById(Long id) {
        return serviceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + id));
    }

    @GetMapping("")
    public String redirectToList() {
        return "redirect:/admin/service/list";
    }
}
