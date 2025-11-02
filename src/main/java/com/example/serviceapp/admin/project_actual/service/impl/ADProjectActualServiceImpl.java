package com.example.serviceapp.admin.project_actual.service.impl;

import com.example.serviceapp.admin.project_actual.repository.ADProjectActualRepository;
import com.example.serviceapp.admin.project_actual.service.ADProjectActualService;
import com.example.serviceapp.common.entity.ProjectActual;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ADProjectActualServiceImpl implements ADProjectActualService {

    public final ADProjectActualRepository projectActualRepository;

    public ADProjectActualServiceImpl(ADProjectActualRepository projectActualRepository) {
        this.projectActualRepository = projectActualRepository;
    }

    @CacheEvict(value = "projectActuals", allEntries = true)
    @Override
    @Transactional
    public void addProjectActual(ProjectActual projectActual) {
        projectActualRepository.save(projectActual);
    }

    @Override
    public Page<ProjectActual> searchProjectActual(String keyword, Pageable pageable) {
        return projectActualRepository.searchProjectActual(keyword, pageable);
    }

    @Override
    public Page<ProjectActual> findAll(Pageable pageable) {
        return projectActualRepository.findAll(pageable);
    }

    @Override
    public ProjectActual findById(Long id) {
        return projectActualRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectActual not found with id: " + id));
    }

    @CacheEvict(value = "projectActuals", allEntries = true)
    @Override
    @Transactional
    public void save(ProjectActual existing) {
        projectActualRepository.save(existing);
    }

    @CacheEvict(value = "projectActuals", allEntries = true)
    @Override
    @Transactional
    public void delete(ProjectActual projectActual) {
        projectActual.setDeleteFlag(1);
        projectActualRepository.save(projectActual);
    }

}
