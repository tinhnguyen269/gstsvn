package com.example.serviceapp.customer.post.controller;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.customer.home.service.HomeService;
import com.example.serviceapp.customer.post.service.PostService;
import com.example.serviceapp.util.DateFormatter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/tin-tuc")
public class PostController {

    private final PostService postService;
    private final HomeService homeService;

    public PostController(PostService postService, HomeService homeService) {
        this.postService = postService;
        this.homeService = homeService;
    }

    @GetMapping("")
    public String showPostForCustomer(Model model,
                                      @RequestParam(defaultValue = "0") int page) {
        int pageSize = 9;
        Page<Post> postPage = postService.findAll(page, pageSize);

        model.addAttribute("Post", postPage.getContent());
        model.addAttribute("postPage", postPage);
        model.addAttribute("currentPage", page + 1); // để hiển thị bắt đầu từ 1
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("dateFormatter", new DateFormatter());

        return "customer/post/post_list";
    }



    @GetMapping("/{slug}")
    public String viewPost(@PathVariable String slug, Model model) {
        Post post = postService.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post slug: " + slug));
        List<Post> Post = homeService.findPost9();
        model.addAttribute("post", post);
        model.addAttribute("Post", Post);
        model.addAttribute("dateFormatter", new DateFormatter());
        model.addAttribute("customer", new com.example.serviceapp.common.entity.Customer());

        return "customer/post/post_detail";
    }

}
