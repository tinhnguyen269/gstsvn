package com.example.serviceapp.customer.post.service.impl;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.customer.post.repositoty.PostRepository;
import org.springframework.stereotype.Service;
import com.example.serviceapp.customer.post.service.PostService;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);

    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAllActivePosts();
    }
}
