package com.example.serviceapp.admin.post.service;

import com.example.serviceapp.common.entity.Post;

import java.util.List;
import java.util.Optional;

public interface ADPostService {
    void save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll();
}
