package com.example.serviceapp.admin.project_actual.repository;

import com.example.serviceapp.common.entity.ProjectActual;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ADProjectActualRepository extends JpaRepository<ProjectActual, Long> {
    @Query("SELECT f FROM ProjectActual f " +
            "WHERE (LOWER(f.nameCustomer) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND f.deleteFlag = 0 " +
            "ORDER BY f.createAt DESC")
    Page<ProjectActual> searchProjectActual(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT f FROM ProjectActual f WHERE f.deleteFlag = 0 ORDER BY f.createAt DESC limit 10")
    List<ProjectActual> findTop10ByOrderByCreateAtDesc();
}
