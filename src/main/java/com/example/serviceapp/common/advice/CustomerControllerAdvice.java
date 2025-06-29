package com.example.serviceapp.common.advice;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.customer.post.service.PostService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(basePackages = "com.example.serviceapp.customer")
public class CustomerControllerAdvice {

    private final PostService postService;

    public CustomerControllerAdvice(PostService postService) {
        this.postService = postService;
    }

    @ModelAttribute("latestPosts")
    public List<Post> latestPosts() {
        return postService.getTop10NewestPosts();
    }
}
