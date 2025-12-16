package com.example.serviceapp.customer.home.service.impl;

import com.example.serviceapp.admin.project_actual.repository.ADProjectActualRepository;
import com.example.serviceapp.admin.post.repository.ADPostRepository;
import com.example.serviceapp.admin.project_image.repository.ADProjectRepository;
import com.example.serviceapp.common.entity.ProjectActual;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.ProjectImage;
import com.example.serviceapp.customer.home.service.HomeService;
import com.example.serviceapp.customer.post.repositoty.PostRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    private final ADPostRepository adPostRepository;
    private final ADProjectActualRepository adFeedbackRepository;
    private final ADProjectRepository adProjectRepository;
    public HomeServiceImpl(PostRepository postRepository, ADPostRepository adPostRepository, ADProjectActualRepository adFeedbackRepository, ADProjectRepository adProjectRepository) {
        this.adPostRepository = adPostRepository;
        this.adFeedbackRepository = adFeedbackRepository;
        this.adProjectRepository = adProjectRepository;
    }


    @Override
    @Cacheable("projectActuals") // cache theo key "feedbacks"
    public List<ProjectActual> findProjectActual10() {
        return adFeedbackRepository.findTop10ByOrderByCreateAtDesc();
    }

    @Override
    @Cacheable("posts") // cache theo key "posts"
    public List<Post> findPost9() {
        return adPostRepository.findTop9ByOrderByCreateAtDesc();
    }

    @Override
    @Cacheable("projects") // cache theo key "projects"
    public List<ProjectImage> findProject() {
        List<ProjectImage> projects = adProjectRepository.findAllByOrderByCreateAtDesc();
        return projects;
    }
}
