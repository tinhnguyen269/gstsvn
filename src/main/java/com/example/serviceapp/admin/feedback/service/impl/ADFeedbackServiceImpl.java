package com.example.serviceapp.admin.feedback.service.impl;

import com.example.serviceapp.admin.feedback.repository.ADFeedbackRepository;
import com.example.serviceapp.admin.feedback.service.ADFeedbackService;
import com.example.serviceapp.common.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ADFeedbackServiceImpl implements ADFeedbackService {

    public final ADFeedbackRepository feedbackRepository;

    public ADFeedbackServiceImpl(ADFeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public void addFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    @Override
    public Page<Feedback> searchFeedback(String keyword, Pageable pageable) {
        return feedbackRepository.searchFeedback(keyword, pageable);
    }

    @Override
    public Page<Feedback> findAll(Pageable pageable) {
        return feedbackRepository.findAll(pageable);
    }

    @Override
    public Feedback findById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
    }

    @Override
    public void save(Feedback existing) {
        feedbackRepository.save(existing);
    }

    @Override
    public void delete(Feedback feedback) {
        feedback.setDeleteFlag(1);
        feedbackRepository.save(feedback);
    }

}
