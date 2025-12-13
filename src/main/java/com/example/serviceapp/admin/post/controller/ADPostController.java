    package com.example.serviceapp.admin.post.controller;

    import com.example.serviceapp.admin.post.service.ADPostService;
    import com.example.serviceapp.common.entity.Post;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;

    import java.text.Normalizer;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Locale;


    @Controller
    @RequestMapping("/admin")
    public class ADPostController {

        private final ADPostService postService;

        public ADPostController(ADPostService postService) {
            this.postService = postService;
        }

        public String toSlug(String input) {
            String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .replaceAll("[^a-zA-Z0-9]+", "-")
                    .toLowerCase(Locale.ROOT)
                    .replaceAll("^-|-$", "");
            return slug;
        }

        @GetMapping("/post/add")
        public String showAddForm(Model model) {
                model.addAttribute("post", new Post());
            return "admin/post/add_post";
        }

        @PostMapping("/post/save")
        public String savePost(@ModelAttribute("post") Post post, BindingResult result) {
            boolean titleExists = postService.existsByTitle(post.getTitle());
            if (titleExists) {
                result.rejectValue("title", "error.post", "Tiêu đề bài viết đã tồn tại. Vui lòng chọn tiêu đề khác!");
            }
            
            if (result.hasErrors()) {
                return "admin/post/add_post";
            }
            
            post.setSlug(toSlug(post.getTitle()));
            post.setCreateAt(LocalDateTime.now());
            postService.save(post);
            return "redirect:/admin/post/list";
        }

        @GetMapping("/post/list")
        public String listPost(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false, defaultValue = "") String keyword) {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
            Page<Post> postPage;

            if (keyword != null && !keyword.isEmpty()) {
                postPage = postService.searchPosts(keyword, pageable);
            } else {
                postPage = postService.findAll(pageable);
            }

            model.addAttribute("postPage", postPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("keyword", keyword);

            return "admin/post/post";
        }

        @GetMapping("/post/edit/{id}")
        public String showEditForm(@PathVariable Long id, Model model) {
            Post post = postService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
            model.addAttribute("post", post);
            return "admin/post/edit_post";
        }

        @PostMapping("/post/update/{id}")
        public String updatePost(@PathVariable Long id, 
                                @ModelAttribute("post") Post updated,
                                BindingResult result, 
                                Model model) {
            Post existingPost = postService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));

            // Chỉ kiểm tra trùng nếu title thay đổi
            if (!existingPost.getTitle().toLowerCase().equals(updated.getTitle().toLowerCase())) {
                boolean titleExists = postService.existsByTitle(updated.getTitle());
                if (titleExists) {
                    result.rejectValue("title", "error.post", "Tiêu đề bài viết đã tồn tại. Vui lòng chọn tiêu đề khác!");
                }
            }
            
            // Nếu có lỗi, giữ lại postId và trả về form
            if (result.hasErrors()) {
                updated.setPostId(id); // Đảm bảo postId được giữ lại
                model.addAttribute("post", updated);
                return "admin/post/edit_post";
            }

            // Cập nhật post
            existingPost.setTitle(updated.getTitle());
            existingPost.setContent(updated.getContent());
            existingPost.setImageUrl(updated.getImageUrl());
            existingPost.setSlug(toSlug(updated.getTitle()));
            existingPost.setUpdateAt(LocalDateTime.now());
            postService.save(existingPost);
            return "redirect:/admin/post/list";
        }
        @PostMapping("/post/delete/{id}")
        public String deletePost(@PathVariable Long id) {
            Post existingPost = postService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));

            existingPost.setDeleteFlag(1);
            postService.save(existingPost);

            return "redirect:/admin/post/list";
        }
        @PostMapping("/post/delSelected")
        public String deleteListPost(@RequestParam("ids") List<Long> ids) {
            postService.softDeletePosts(ids);
            return "redirect:/admin/post/list";
        }

    }
