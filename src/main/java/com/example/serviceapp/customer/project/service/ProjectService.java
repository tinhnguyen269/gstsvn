package com.example.serviceapp.customer.project.service;

import com.example.serviceapp.common.entity.Project;

import java.util.Optional;

public interface ProjectService {
    Optional<Project> findAllImageByProject(Long projectId);
}
