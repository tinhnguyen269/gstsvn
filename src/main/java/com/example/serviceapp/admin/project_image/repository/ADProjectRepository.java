package com.example.serviceapp.admin.project_image.repository;

import com.example.serviceapp.common.entity.ProjectImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ADProjectRepository extends JpaRepository<ProjectImage, Long> {

    @Query("SELECT p FROM ProjectImage p WHERE LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.deleteFlag = 0 ORDER BY p.createAt DESC")
    Page<ProjectImage> searchProjects(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM ProjectImage p WHERE p.deleteFlag = 0 ORDER BY p.createAt DESC limit 9")
    List<ProjectImage> findTop9ByOrderByCreateAtDesc();

    @Modifying
    @Transactional
    @Query("UPDATE ProjectImage p SET p.deleteFlag = 1 WHERE p.projectId IN :ids")
    void softDeleteProjects(List<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectImage p SET p.deleteFlag = 1 WHERE p.projectId = :id")
    void softDeleteById(Long id);
}
