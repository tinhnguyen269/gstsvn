package com.example.serviceapp.admin.project_actual.controller;

import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.admin.project_actual.service.ADProjectActualService;
import com.example.serviceapp.common.entity.ProjectActual;
import com.example.serviceapp.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ADProjectActualController {

    public final ADProjectActualService projectActualService;
    private final UserRepository userRepository;

    public ADProjectActualController(ADProjectActualService projectActualService, UserRepository userRepository) {
        this.projectActualService = projectActualService;
        this.userRepository = userRepository;
    }


    @PostMapping("/projectActual/add")
    public String doAdd(ProjectActual projectActual) {
        // Lấy thông tin user đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            userRepository.findByUsernameOrPhoneNumber(username).ifPresent(user -> {
                projectActual.setCreateBy(user.getUserId());
            });
        }
        projectActualService.addProjectActual(projectActual);
        return "redirect:/admin/projectActual/list";
    }


    @GetMapping("/projectActual/list")
    public String listProjectActual(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false, defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<ProjectActual> projectActualPage;

        if (keyword != null && !keyword.isEmpty()) {
            projectActualPage = projectActualService.searchProjectActual(keyword, pageable);
        } else {
            projectActualPage = projectActualService.findAll(pageable);
        }

        // Tạo map chứa thông tin user (createBy và updateBy)
        Map<Long, User> userMap = new HashMap<>();
        for (ProjectActual projectActual : projectActualPage.getContent()) {
            if (projectActual.getCreateBy() != null) {
                userRepository.findUserByIdById(projectActual.getCreateBy()).ifPresent(user -> {
                    userMap.put(projectActual.getCreateBy(), user);
                });
            }
            if (projectActual.getUpdateBy() != null && !projectActual.getUpdateBy().equals(projectActual.getCreateBy())) {
                userRepository.findUserByIdById(projectActual.getUpdateBy()).ifPresent(user -> {
                    userMap.put(projectActual.getUpdateBy(), user);
                });
            }
        }

        model.addAttribute("projectActualPage", projectActualPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("projectActualNew", new ProjectActual());
        model.addAttribute("userMap", userMap);

        return "admin/projectActual/projectActual";
    }

    @GetMapping("/projectActual/edit/{id}")
    @ResponseBody
    public ResponseEntity<ProjectActual> getProjectActualById(@PathVariable Long id, Model model) {
        ProjectActual projectActual = projectActualService.findById(id);
        if (projectActual != null) {
            return ResponseEntity.ok(projectActual);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/projectActual/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateProjectActual(@PathVariable Long id, @RequestBody ProjectActual updatedProjectActual) {
        ProjectActual existing = projectActualService.findById(id);
        if (existing != null) {
            // Lấy thông tin user đăng nhập
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                userRepository.findByUsernameOrPhoneNumber(username).ifPresent(user -> {
                    existing.setUpdateBy(user.getUserId());
                });
            }
            
            existing.setNameCustomer(updatedProjectActual.getNameCustomer());
            existing.setContent(updatedProjectActual.getContent());
            existing.setLinkYoutube(updatedProjectActual.getLinkYoutube());
            projectActualService.save(existing);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/projectActual/delete/{id}")
    public String deleteProjectActual(@PathVariable Long id) {
        ProjectActual projectActual = projectActualService.findById(id);
        if (projectActual != null) {
            projectActualService.delete(projectActual);
        }
        return "redirect:/admin/projectActual/list";


    }
}

