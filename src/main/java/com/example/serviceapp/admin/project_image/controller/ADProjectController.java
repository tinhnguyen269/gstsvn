package com.example.serviceapp.admin.project_image.controller;

import com.example.serviceapp.admin.project_image.service.ADImageProjectService;
import com.example.serviceapp.admin.project_image.service.ADProjectService;
import com.example.serviceapp.common.entity.ImageProject;
import com.example.serviceapp.common.entity.ProjectImage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ADProjectController {

    private final ADProjectService projectService;
    private final ADImageProjectService imageProjectService;

    public ADProjectController(ADProjectService projectService,
                               ADImageProjectService imageProjectService) {
        this.projectService = projectService;
        this.imageProjectService = imageProjectService;
    }

    // --- Thêm dự án ---
    @GetMapping("/project/add")
    public String showAddForm(Model model) {
        model.addAttribute("project", new ProjectImage());
        return "admin/project/form_project";
    }

    @PostMapping("/project/save")
    public String saveProject(@ModelAttribute ProjectImage project,
                              @RequestParam("imageProjects") String imageJson) throws JsonProcessingException {
        // Lưu project trước để có ID
        ProjectImage savedProject = projectService.save(project);

        // Parse danh sách URL ảnh
        ObjectMapper mapper = new ObjectMapper();
        List<String> imageUrls = mapper.readValue(imageJson, new TypeReference<List<String>>() {});

        // Lưu danh sách ảnh
        for (String url : imageUrls) {
            ImageProject img = new ImageProject();
            img.setImageProjectUrl(url);
            img.setProjectId(savedProject.getProjectId());
            imageProjectService.save(img);
        }

        return "redirect:/admin/project/list";
    }

    // --- Danh sách dự án ---
    @GetMapping("/project/list")
    public String listProject(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false, defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<ProjectImage> projectPage = keyword != null && !keyword.isEmpty()
                ? projectService.searchProjects(keyword, pageable)
                : projectService.findAll(pageable);

        model.addAttribute("projectPage", projectPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);

        return "admin/project/project_list";
    }

    // --- Sửa dự án ---
    @GetMapping("/project/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) throws JsonProcessingException {
        ProjectImage project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));

        // Lấy danh sách ảnh từ bảng ImageProject
        List<ImageProject> images = imageProjectService.findByProjectId(id);
        List<String> imageUrls = images.stream().map(ImageProject::getImageProjectUrl).toList();

        ObjectMapper mapper = new ObjectMapper();
        String imageUrlsJson = mapper.writeValueAsString(imageUrls);

        model.addAttribute("project", project);
        model.addAttribute("imageUrlsJson", imageUrlsJson);

        return "admin/project/form_project";
    }

    @PostMapping("/project/update")
    public String updateProject(@ModelAttribute ProjectImage project,
                                @RequestParam("imageProjects") String imageJson,
                                @RequestParam("thumbnailUrl") String thumbnailUrl) throws JsonProcessingException {

        ProjectImage existingProject = projectService.findById(project.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + project.getProjectId()));

        existingProject.setProjectName(project.getProjectName());
        existingProject.setThumbnailUrl(thumbnailUrl);
        existingProject.setSlug(project.getSlug());
        projectService.save(existingProject);

        // Cập nhật ảnh
        imageProjectService.deleteByProjectId(existingProject.getProjectId()); // xóa ảnh cũ

        ObjectMapper mapper = new ObjectMapper();
        List<String> imageUrls = mapper.readValue(imageJson, new TypeReference<List<String>>() {});

        for (String url : imageUrls) {
            ImageProject img = new ImageProject();
            img.setImageProjectUrl(url);
            img.setProjectId(existingProject.getProjectId());
            imageProjectService.save(img);
        }

        return "redirect:/admin/project/list";
    }

    // --- Xóa 1 dự án ---
    @PostMapping("/project/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.softDeleteById(id);
        imageProjectService.deleteByProjectId(id);
        return ResponseEntity.ok().build();
    }

    // --- Xóa nhiều dự án ---
    @PostMapping("/project/delSelected")
    public String deleteListProject(@RequestParam("ids") List<Long> ids) {
        projectService.softDeleteProjects(ids);
        imageProjectService.deleteByProjectIds(ids);
        return "redirect:/admin/project/list";
    }
}
