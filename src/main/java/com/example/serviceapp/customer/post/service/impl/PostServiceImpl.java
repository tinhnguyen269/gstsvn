package com.example.serviceapp.customer.post.service.impl;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.customer.post.repositoty.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Optional<Post> findBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    @Override
    public Page<Post> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
        return postRepository.findAll(pageable);
    }

}
