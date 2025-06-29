package com.example.serviceapp.admin.service.service;

import com.example.serviceapp.common.entity.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ServiceService {
    void save(Services services);

    Optional<Services> findById(Long id);

    Page<Services> findAll(Pageable pageable);

    Page<Services> searchServices(String keyword, Pageable pageable);
}
