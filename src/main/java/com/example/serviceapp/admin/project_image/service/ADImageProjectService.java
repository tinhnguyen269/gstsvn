package com.example.serviceapp.admin.project_image.service;

import com.example.serviceapp.common.entity.ImageProject;

import java.util.List;

public interface ADImageProjectService {
    List<ImageProject> findByProjectId(Long id);

    void deleteByProjectId(Long projectId);

    ImageProject save(ImageProject img);

    void deleteByProjectIds(List<Long> ids);
}
