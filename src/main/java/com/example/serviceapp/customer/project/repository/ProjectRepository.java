package com.example.serviceapp.customer.project.repository;

import com.example.serviceapp.common.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Define any custom query methods if needed
}
