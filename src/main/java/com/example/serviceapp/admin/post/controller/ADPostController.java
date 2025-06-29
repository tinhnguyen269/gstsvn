package com.example.serviceapp.admin.post.controller;

import com.example.serviceapp.admin.post.service.ADPostService;
import com.example.serviceapp.common.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/admin/post")
public class ADPostController {

    private final ADPostService postService;

    public ADPostController(ADPostService postService) {
        this.postService = postService;
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
            model.addAttribute("post", new Post());
        return "admin/add_post";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute Post post) {
        post.setCreatedAt(LocalDateTime.now());
        postService.save(post);
        return "redirect:/admin/post/list";
    }

    @GetMapping("/list")
    public String listPost(Model model) {
        model.addAttribute("Post", postService.findAll());
        return "admin/post";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Post post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
        model.addAttribute("post", post);
        return "admin/edit_post";
    }

    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post) {
        Post existingPost = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());

        postService.save(existingPost);
        return "redirect:/admin/post/list";
    }


    @GetMapping("/Post")
    public String showPostForCustomer(Model model) {
        List<Post> Post = postService.findAll();
        model.addAttribute("Post", Post);
        return "customer_post_list";
    }

    @GetMapping("/Post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
        model.addAttribute("post", post);
        return "customer_post_detail";
    }


}
