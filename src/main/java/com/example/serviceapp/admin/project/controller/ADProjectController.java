    package com.example.serviceapp.admin.project.controller;

    import com.example.serviceapp.admin.project.service.ADProjectService;
    import com.example.serviceapp.common.entity.ImageProject;
    import com.example.serviceapp.common.entity.Project;
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
    import org.springframework.web.bind.WebDataBinder;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.stream.Collectors;


    @Controller
    @RequestMapping("/admin")
    public class ADProjectController {

        private final ADProjectService projectService;

        public ADProjectController(ADProjectService projectService) {
            this.projectService = projectService;
        }


        @GetMapping("/project/add")
        public String showAddForm(Model model) {
                model.addAttribute("project", new Project());
            return "admin/project/form_project";
        }

        @PostMapping("/project/save")
        public String saveProject(@ModelAttribute Project project,
                                  @RequestParam("imageProjects") String imageJson) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            List<String> imageUrls = mapper.readValue(imageJson, new TypeReference<List<String>>() {});

            List<ImageProject> images = imageUrls.stream()
                    .map(url -> {
                        ImageProject img = new ImageProject();
                        img.setImageProjectUrl(url);
                        img.setProject(project);
                        return img;
                    }).collect(Collectors.toList());

            project.setImageProjects(images);

            projectService.save(project);
            return "redirect:/admin/project/list";
        }

        @InitBinder
        protected void initBinder(WebDataBinder binder) {
            binder.setDisallowedFields("imageProjects");
        }

        @GetMapping("/project/list")
        public String listProject(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false, defaultValue = "") String keyword) {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
            Page<Project> projectPage;

            if (keyword != null && !keyword.isEmpty()) {
                projectPage = projectService.searchProjects(keyword, pageable);
            } else {
                projectPage = projectService.findAll(pageable);
            }

            model.addAttribute("projectPage", projectPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("keyword", keyword);

            return "admin/project/project_list";
        }
        @GetMapping("/project/edit/{id}")
        public String showEditForm(@PathVariable Long id, Model model) throws JsonProcessingException {
            Project project = projectService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));

            // Chuyển danh sách ảnh thành danh sách URL
            List<String> imageUrls = project.getImageProjects().stream()
                    .map(ImageProject::getImageProjectUrl)
                    .collect(Collectors.toList());

            // Chuyển sang JSON string để truyền vào input hidden
            ObjectMapper mapper = new ObjectMapper();
            String imageUrlsJson = mapper.writeValueAsString(imageUrls);

            model.addAttribute("project", project);
            model.addAttribute("imageUrlsJson", imageUrlsJson);

            return "admin/project/form_project";
        }

        @PostMapping("/project/update")
        public String updateProject(@ModelAttribute Project project,
                                    @RequestParam("imageProjects") String imageJson,
                                    @RequestParam("thumbnailUrl") String thumbnailUrl) throws JsonProcessingException {
            Project existingProject = projectService.findById(project.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + project.getProjectId()));

            existingProject.setProjectName(project.getProjectName());
            existingProject.setThumbnailUrl(thumbnailUrl);
            existingProject.setSlug(project.getSlug());

            // Parse danh sách ảnh dự án
            ObjectMapper mapper = new ObjectMapper();
            List<String> imageUrls = mapper.readValue(imageJson, new TypeReference<List<String>>() {});

            List<ImageProject> newImages = imageUrls.stream().map(url -> {
                ImageProject img = new ImageProject();
                img.setImageProjectUrl(url);
                img.setProject(existingProject);
                return img;
            }).collect(Collectors.toList());

            existingProject.getImageProjects().clear(); // Xóa ảnh cũ
            existingProject.getImageProjects().addAll(newImages); // Thêm ảnh mới

            projectService.save(existingProject);
            return "redirect:/admin/project/list";
        }


        @PostMapping("/project/delete/{id}")
        @ResponseBody
        public ResponseEntity<?> deleteProject(@PathVariable Long id) {
            Project existingProject = projectService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));

            existingProject.setDeleteFlag(1);
            projectService.save(existingProject);

            return ResponseEntity.ok().build();
        }
        @PostMapping("/project/delSelected")
        public String deleteListproject(@RequestParam("ids") List<Long> ids) {
            projectService.softDeleteProjects(ids);
            return "redirect:/admin/project/list";
        }


    }
