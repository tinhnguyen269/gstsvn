package com.example.serviceapp.admin.project_image.service.impl;

import com.example.serviceapp.admin.project_image.repository.ADProjectRepository;
import com.example.serviceapp.admin.project_image.service.ADProjectService;
import com.example.serviceapp.common.entity.ProjectImage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ADProjectServiceImpl implements ADProjectService {

    private final ADProjectRepository projectRepository;

    public ADProjectServiceImpl(ADProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @CacheEvict(value = "projects", allEntries = true)
    @Override
    @Transactional
    public ProjectImage save(ProjectImage project) {
        ProjectImage projectSave = projectRepository.save(project);
        return projectSave;
    }

    @Override
    public Optional<ProjectImage> findById(Long id) {
        return projectRepository.findById(id);

    }

    @Override
    public Page<ProjectImage> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Page<ProjectImage> searchProjects(String keyword, Pageable pageable) {
        return projectRepository.searchProjects(keyword, pageable);
    }

    @CacheEvict(value = "projects", allEntries = true)
    @Override
    @Transactional
    public void softDeleteProjects(List<Long> ids) {
        projectRepository.softDeleteProjects(ids);
    }

    @CacheEvict(value = "projects", allEntries = true)
    @Override
    @Transactional
    public void softDeleteById(Long id) {
        projectRepository.softDeleteById(id);
    }

}
