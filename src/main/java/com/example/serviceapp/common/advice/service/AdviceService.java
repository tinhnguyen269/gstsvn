package com.example.serviceapp.common.advice.service;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Services;

import java.util.List;

public interface AdviceService {
    List<Post> getTop10NewestPosts();

    List<Services> findAll();
}
