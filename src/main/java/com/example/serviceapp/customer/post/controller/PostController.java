package com.example.serviceapp.customer.post.controller;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.customer.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/Post")
    public String showPostForCustomer(Model model) {
        List<Post> Post = postService.findAll();
        model.addAttribute("Post", Post);
        return "customer/post_list";
    }

    @GetMapping("/Post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
        model.addAttribute("post", post);

        return "customer/post_detail";
    }

}
