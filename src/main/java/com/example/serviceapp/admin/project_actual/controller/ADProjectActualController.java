package com.example.serviceapp.admin.project_actual.controller;

import com.example.serviceapp.admin.project_actual.service.ADProjectActualService;
import com.example.serviceapp.common.entity.ProjectActual;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class ADProjectActualController {

    public final ADProjectActualService projectActualService;

    public ADProjectActualController(ADProjectActualService projectActualService) {
        this.projectActualService = projectActualService;
    }


    @PostMapping("/projectActual/add")
    public String doAdd(ProjectActual projectActual) {
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

        model.addAttribute("projectActualPage", projectActualPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("projectActualNew", new ProjectActual());

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

