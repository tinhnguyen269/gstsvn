package com.example.serviceapp.customer.service.service.impl;

import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.service.repository.ServiceRepository;
import com.example.serviceapp.customer.service.service.ServiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository servicesRepository;

    public ServiceServiceImpl(ServiceRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    @Override
    public Optional<Services> findBySlug(String slug) {
        return servicesRepository.findBySlug(slug);

    }

}
