package com.example.serviceapp.admin.project.service.impl;

import com.example.serviceapp.admin.project.repository.ADProjectRepository;
import com.example.serviceapp.admin.project.service.ADProjectService;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ADProjectServiceImpl implements ADProjectService {

    private final ADProjectRepository projectRepository;
    public ADProjectServiceImpl(ADProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    @Override
    @Transactional
    public void save(Project project) {
        projectRepository.save(project);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);

    }

    @Override
    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Page<Project> searchProjects(String keyword, Pageable pageable) {
        return projectRepository.searchProjects(keyword, pageable);
    }

}
