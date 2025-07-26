package com.example.serviceapp.customer.home.service.impl;

import com.example.serviceapp.admin.feedback.repository.ADFeedbackRepository;
import com.example.serviceapp.common.entity.Feedback;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.customer.home.service.HomeService;
import com.example.serviceapp.customer.post.repositoty.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    private final PostRepository postRepository;
    private final ADFeedbackRepository adFeedbackRepository;
    public HomeServiceImpl(PostRepository postRepository, ADFeedbackRepository adFeedbackRepository) {
        this.postRepository = postRepository;
        this.adFeedbackRepository = adFeedbackRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAllActivePosts();
    }

    @Override
    public List<Feedback> findAllFeedback() {
        return adFeedbackRepository.findAll();
    }
}
