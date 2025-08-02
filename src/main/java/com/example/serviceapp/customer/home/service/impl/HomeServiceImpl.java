package com.example.serviceapp.customer.home.service.impl;

import com.example.serviceapp.admin.feedback.repository.ADFeedbackRepository;
import com.example.serviceapp.admin.post.repository.ADPostRepository;
import com.example.serviceapp.admin.project.repository.ADProjectRepository;
import com.example.serviceapp.common.entity.Feedback;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Project;
import com.example.serviceapp.customer.home.service.HomeService;
import com.example.serviceapp.customer.post.repositoty.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    private final ADPostRepository adPostRepository;
    private final ADFeedbackRepository adFeedbackRepository;
    private final ADProjectRepository adProjectRepository;
    public HomeServiceImpl(PostRepository postRepository, ADPostRepository adPostRepository, ADFeedbackRepository adFeedbackRepository, ADProjectRepository adProjectRepository) {
        this.adPostRepository = adPostRepository;
        this.adFeedbackRepository = adFeedbackRepository;
        this.adProjectRepository = adProjectRepository;
    }


    @Override
    public List<Feedback> findFeedback10() {
        return adFeedbackRepository.findTop10ByOrderByCreateAtDesc();
    }

    @Override
    public List<Post> findPost9() {
        return adPostRepository.findTop9ByOrderByCreateAtDesc();
    }

    @Override
    public List<Project> findProject9() {
        return adProjectRepository.findTop9ByOrderByCreateAtDesc();
    }
}
