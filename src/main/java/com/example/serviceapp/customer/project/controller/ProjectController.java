package com.example.serviceapp.customer.project.controller;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Project;
import com.example.serviceapp.customer.home.service.HomeService;
import com.example.serviceapp.customer.project.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/du-an")
public class ProjectController {

    private final ProjectService projectService;
    private final HomeService homeService;

    public ProjectController(ProjectService projectService, HomeService homeService) {
        this.projectService = projectService;
        this.homeService = homeService;
    }

    @GetMapping("/{slug}")
    public String findAllImageByProject(@PathVariable String slug, Model model) {
        Optional<Project> project = projectService.findAllImageBySlug(slug);
        if (project.isPresent()) {
            model.addAttribute("project", project.get());
            model.addAttribute("images", project.get().getImageProjects());
        } else {
            return "redirect:/not-found";
        }
        List<Post> Post = homeService.findPost9();
        model.addAttribute("Post", Post);
        return "customer/project/project_detail";
    }

}
