package com.example.serviceapp.admin.feedback.repository;

import com.example.serviceapp.common.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ADFeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query("SELECT f FROM Feedback f " +
            "WHERE (LOWER(f.nameCustomer) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND f.deleteFlag = 0 " +
            "ORDER BY f.createAt DESC")
    Page<Feedback> searchFeedback(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE f.deleteFlag = 0 ORDER BY f.createAt DESC limit 10")
    List<Feedback> findTop10ByOrderByCreateAtDesc();
}
