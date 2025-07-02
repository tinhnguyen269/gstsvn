package com.example.serviceapp.common.advice.service.impl;

import com.example.serviceapp.common.advice.service.AdviceService;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.post.repositoty.PostRepository;
import com.example.serviceapp.customer.service.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdviceServiceImpl implements AdviceService {

    private final PostRepository postRepository;

    private  final ServiceRepository serviceRepository;

    public AdviceServiceImpl(PostRepository postRepository, ServiceRepository serviceRepository) {
        this.postRepository = postRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<Post> getTop10NewestPosts() {
        return postRepository.findTop10NewestPosts();
    }

    @Override
    public List<Services> findAll() {
        return serviceRepository.getAllService();
    }
}
