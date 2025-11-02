package com.example.serviceapp.admin.project_image.repository;

import com.example.serviceapp.common.entity.ImageProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageProjectRepository extends JpaRepository<ImageProject, Long> {
    List<ImageProject> findByProjectId(Long projectId);

    @Modifying
    @Transactional
    void deleteByProjectId(Long projectId);
}

