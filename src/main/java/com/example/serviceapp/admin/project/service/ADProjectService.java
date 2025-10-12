package com.example.serviceapp.admin.project.service;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ADProjectService {
    void save(Project project);

    Optional<Project> findById(Long id);

    Page<Project> findAll(Pageable pageable);

    Page<Project> searchProjects(String keyword, Pageable pageable);

    void softDeleteProjects(List<Long> ids);
}
