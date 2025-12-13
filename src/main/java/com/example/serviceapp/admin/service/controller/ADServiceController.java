package com.example.serviceapp.admin.service.controller;

import com.example.serviceapp.admin.service.service.ADServiceService;
import com.example.serviceapp.common.entity.Services;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin/service")
public class ADServiceController {

    private final ADServiceService serviceService;

    public ADServiceController(ADServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public String toSlug(String input) {
        String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^a-zA-Z0-9]+", "-")
                .toLowerCase(Locale.ROOT)
                .replaceAll("^-|-$", "");
        return slug;
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
    public String saveService(@ModelAttribute("service") Services service, 
                              BindingResult result) {

        boolean serviceNameExists = serviceService.existsByServiceName(service.getServiceName());
        if (serviceNameExists) {
            result.rejectValue("serviceName", "error.service", "Tên dịch vụ đã tồn tại. Vui lòng chọn tên khác!");
        }
        
        if (result.hasErrors()) {
            return "admin/service/add_service";
        }
        
        service.setSlug(toSlug(service.getServiceName()));
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
    public String updateService(@PathVariable Long id, 
                               @ModelAttribute("service") Services updated,
                               BindingResult result, 
                               Model model) {

        Services existingService = getServiceById(id);

        // Chỉ kiểm tra trùng nếu serviceName thay đổi
        if (!existingService.getServiceName().toLowerCase().equals(updated.getServiceName().toLowerCase())) {
            boolean serviceNameExists = serviceService.existsByServiceName(updated.getServiceName());
            if (serviceNameExists) {
                result.rejectValue("serviceName", "error.service", "Tên dịch vụ đã tồn tại. Vui lòng chọn tên khác!");
            }
        }
        
        // Nếu có lỗi, giữ lại serviceId và trả về form
        if (result.hasErrors()) {
            updated.setServiceId(id); // Đảm bảo serviceId được giữ lại
            model.addAttribute("service", updated);
            return "admin/service/edit_service";
        }

        // Cập nhật service
        Services service = getServiceById(id);
        service.setServiceName(updated.getServiceName());
        service.setContent(updated.getContent());
        service.setIcon(updated.getIcon());
        service.setImgPrice(updated.getImgPrice());
        service.setSlug(toSlug(updated.getServiceName()));
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
