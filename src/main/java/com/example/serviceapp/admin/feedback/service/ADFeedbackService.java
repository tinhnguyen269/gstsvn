package com.example.serviceapp.admin.feedback.service;

import com.example.serviceapp.common.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ADFeedbackService {
    void addFeedback(Feedback feedback);

    Page<Feedback> searchFeedback(String keyword, Pageable pageable);

    Page<Feedback> findAll(Pageable pageable);

    Feedback findById(Long id);

    void save(Feedback existing);

    void delete(Feedback feedback);
}
