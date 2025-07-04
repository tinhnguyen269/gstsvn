package com.example.serviceapp.customer.home.service.impl;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.home.repository.HomeRepository;
import com.example.serviceapp.customer.home.service.HomeService;
import com.example.serviceapp.customer.post.repositoty.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    private final PostRepository postRepository;

    public HomeServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAllActivePosts();
    }
}
