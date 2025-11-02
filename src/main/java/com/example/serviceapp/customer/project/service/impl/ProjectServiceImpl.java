package com.example.serviceapp.customer.project.service.impl;

import com.example.serviceapp.common.entity.ProjectImage;
import com.example.serviceapp.customer.project.repository.ProjectRepository;
import com.example.serviceapp.customer.project.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<ProjectImage> findAllImageBySlug(String slug) {
        return projectRepository.findBySlug(slug);
    }

}
