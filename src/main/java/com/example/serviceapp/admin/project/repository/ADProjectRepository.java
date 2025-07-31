package com.example.serviceapp.admin.project.repository;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ADProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.deleteFlag = 0 ORDER BY p.createAt DESC")
    Page<Project> searchProjects(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.deleteFlag = 0 ORDER BY p.createAt DESC limit 9")
    List<Project> findTop9ByOrderByCreateAtDesc();
}
