package com.example.serviceapp.customer.project.controller;

import com.example.serviceapp.admin.project_image.repository.ImageProjectRepository;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.ProjectImage;
import com.example.serviceapp.common.entity.ImageProject;
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
    private final ImageProjectRepository imageProjectRepository;

    public ProjectController(ProjectService projectService,
                             HomeService homeService,
                             ImageProjectRepository imageProjectRepository) {
        this.projectService = projectService;
        this.homeService = homeService;
        this.imageProjectRepository = imageProjectRepository;
    }

    @GetMapping("/{slug}")
    public String findAllImageByProject(@PathVariable String slug, Model model) {
        Optional<ProjectImage> projectOpt = projectService.findAllImageBySlug(slug);

        if (projectOpt.isEmpty()) {
            return "redirect:/not-found";
        }

        ProjectImage project = projectOpt.get();
        List<ImageProject> images = imageProjectRepository.findByProjectId(project.getProjectId());

        model.addAttribute("project", project);
        model.addAttribute("images", images);

        List<Post> posts = homeService.findPost9();
        model.addAttribute("Post", posts);

        return "customer/project/project_detail";
    }
}
