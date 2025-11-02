package com.example.serviceapp.customer.project.service;

import com.example.serviceapp.common.entity.ProjectImage;

import java.util.Optional;

public interface ProjectService {
    Optional<ProjectImage> findAllImageBySlug(String slug);
}
