package com.example.serviceapp.admin.service.service.impl;

import com.example.serviceapp.admin.service.repository.ServiceRepository;
import com.example.serviceapp.admin.service.service.ServiceService;
import com.example.serviceapp.common.entity.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService {


    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void save(Services service) {
        serviceRepository.save(service);
    }

    @Override
    public Optional<Services> findById(Long id) {
        return serviceRepository.findById(id);

    }

    @Override
    public Page<Services> findAll(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    @Override
    public Page<Services> searchServices(String keyword, Pageable pageable) {
        return serviceRepository.searchServices(keyword, pageable);
    }
}
