package com.example.serviceapp.admin.post.service;

import com.example.serviceapp.common.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ADPostService {
    void save(Post post);

    Optional<Post> findById(Long id);

    Page<Post> findAll(Pageable pageable);

    Page<Post> searchPosts(String keyword, Pageable pageable);

    void softDeletePosts(List<Long> ids);

    boolean existsByTitle(String title);
}
