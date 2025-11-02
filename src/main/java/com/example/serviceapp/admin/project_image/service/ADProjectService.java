package com.example.serviceapp.admin.project_image.service;

import com.example.serviceapp.common.entity.ProjectImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ADProjectService {

    Optional<ProjectImage> findById(Long id);

    Page<ProjectImage> findAll(Pageable pageable);

    Page<ProjectImage> searchProjects(String keyword, Pageable pageable);

    void softDeleteProjects(List<Long> ids);

    void softDeleteById(Long id);

    ProjectImage save(ProjectImage project);
}
