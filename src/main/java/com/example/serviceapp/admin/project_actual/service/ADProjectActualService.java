package com.example.serviceapp.admin.project_actual.service;

import com.example.serviceapp.common.entity.ProjectActual;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ADProjectActualService {
    void addProjectActual(ProjectActual projectActual);

    Page<ProjectActual> searchProjectActual(String keyword, Pageable pageable);

    Page<ProjectActual> findAll(Pageable pageable);

    ProjectActual findById(Long id);

    void save(ProjectActual existing);

    void delete(ProjectActual projectActual);
}
