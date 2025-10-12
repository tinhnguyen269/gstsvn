package com.example.serviceapp.admin.post.service.impl;

import com.example.serviceapp.admin.post.repository.ADPostRepository;
import com.example.serviceapp.admin.post.service.ADPostService;
import com.example.serviceapp.common.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ADPostServiceImpl implements ADPostService {

    private final ADPostRepository postRepository;
    public ADPostServiceImpl(ADPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);

    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchPosts(keyword, pageable);
    }

    @Override
    @Transactional
    public void softDeletePosts(List<Long> ids) {
        postRepository.softDeletePosts(ids);
    }

}
