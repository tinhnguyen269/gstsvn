package com.example.serviceapp.admin.feedback.controller;

import com.example.serviceapp.admin.feedback.service.ADFeedbackService;
import com.example.serviceapp.common.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class ADFeedbackController {

    public final ADFeedbackService feedbackService;

    public ADFeedbackController(ADFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }


    @PostMapping("/feedback/add")
    public String doAdd(Feedback feedback) {
        feedbackService.addFeedback(feedback);
        return "redirect:/admin/feedback/list";
    }


    @GetMapping("/feedback/list")
    public String listFeedback(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false, defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Feedback> feedbackPage;

        if (keyword != null && !keyword.isEmpty()) {
            feedbackPage = feedbackService.searchFeedback(keyword, pageable);
        } else {
            feedbackPage = feedbackService.findAll(pageable);
        }

        model.addAttribute("feedbackPage", feedbackPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("feedbackNew", new Feedback());

        return "admin/feedback/feedback";
    }

    @GetMapping("/feedback/edit/{id}")
    @ResponseBody
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id, Model model) {
        Feedback feedback = feedbackService.findById(id);
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/feedback/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateFeedback(@PathVariable Long id, @RequestBody Feedback updatedFeedback) {
        Feedback existing = feedbackService.findById(id);
        if (existing != null) {
            existing.setNameCustomer(updatedFeedback.getNameCustomer());
            existing.setContent(updatedFeedback.getContent());
            existing.setLinkYoutube(updatedFeedback.getLinkYoutube());
            feedbackService.save(existing);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/feedback/delete/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        Feedback feedback = feedbackService.findById(id);
        if (feedback != null) {
            feedbackService.delete(feedback);
        }
        return "redirect:/admin/feedback/list";


    }
}

