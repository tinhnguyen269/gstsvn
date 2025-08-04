    package com.example.serviceapp.admin.post.controller;

    import com.example.serviceapp.admin.post.service.ADPostService;
    import com.example.serviceapp.common.entity.Post;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDateTime;


    @Controller
    @RequestMapping("/admin")
    public class ADPostController {

        private final ADPostService postService;

        public ADPostController(ADPostService postService) {
            this.postService = postService;
        }


        @GetMapping("/post/add")
        public String showAddForm(Model model) {
                model.addAttribute("post", new Post());
            return "admin/post/add_post";
        }

        @PostMapping("/post/save")
        public String savePost(@ModelAttribute Post post) {
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
        public String updatePost(@PathVariable Long id, @ModelAttribute Post post) {
            Post existingPost = postService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));

            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            existingPost.setImageUrl(post.getImageUrl());

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


    }
