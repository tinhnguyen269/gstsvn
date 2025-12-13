    package com.example.serviceapp.admin.post.controller;

    import com.example.serviceapp.admin.authenticate.repository.UserRepository;
    import com.example.serviceapp.admin.post.service.ADPostService;
    import com.example.serviceapp.common.entity.Post;
    import com.example.serviceapp.common.entity.User;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;

    import java.text.Normalizer;
    import java.time.LocalDateTime;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;


    @Controller
    @RequestMapping("/admin")
    public class ADPostController {

        private final ADPostService postService;
        private final UserRepository userRepository;

        public ADPostController(ADPostService postService, UserRepository userRepository) {
            this.postService = postService;
            this.userRepository = userRepository;
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
            
            // Lấy thông tin user đăng nhập
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                userRepository.findByUsernameOrPhoneNumber(username).ifPresent(user -> {
                    post.setCreateBy(user.getUserId());
                });
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

            // Tạo map chứa thông tin user (createBy và updateBy)
            Map<Long, User> userMap = new HashMap<>();
            for (Post post : postPage.getContent()) {
                if (post.getCreateBy() != null) {
                    userRepository.findUserByIdById(post.getCreateBy()).ifPresent(user -> {
                        userMap.put(post.getCreateBy(), user);
                    });
                }
                if (post.getUpdateBy() != null && !post.getUpdateBy().equals(post.getCreateBy())) {
                    userRepository.findUserByIdById(post.getUpdateBy()).ifPresent(user -> {
                        userMap.put(post.getUpdateBy(), user);
                    });
                }
            }

            model.addAttribute("postPage", postPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("keyword", keyword);
            model.addAttribute("userMap", userMap);

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

            // Lấy thông tin user đăng nhập
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                userRepository.findByUsernameOrPhoneNumber(username).ifPresent(user -> {
                    existingPost.setUpdateBy(user.getUserId());
                });
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
