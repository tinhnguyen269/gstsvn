package com.example.serviceapp.customer.service.service;

import com.example.serviceapp.common.entity.Services;

import java.util.List;
import java.util.Optional;

public interface ServiceService {

    Optional<Services> findBySlug(String slug);
}
