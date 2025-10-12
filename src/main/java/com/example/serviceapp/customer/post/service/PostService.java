package com.example.serviceapp.customer.post.service;

import com.example.serviceapp.common.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostService {


    Page<Post> findAll(int page, int pageSize);
    Optional<Post> findBySlug(String slug);
}
