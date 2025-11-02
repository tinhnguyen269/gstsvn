package com.example.serviceapp.customer.project.repository;

import com.example.serviceapp.common.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectImage, Long> {
    Optional<ProjectImage> findBySlug(String slug);
}
