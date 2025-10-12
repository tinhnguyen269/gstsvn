package com.example.serviceapp.admin.service.service.impl;

import com.example.serviceapp.admin.service.repository.ADServiceRepository;
import com.example.serviceapp.admin.service.service.ADServiceService;
import com.example.serviceapp.common.entity.Services;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ADServiceServiceImpl implements ADServiceService {


    private final ADServiceRepository serviceRepository;

    public ADServiceServiceImpl(ADServiceRepository serviceRepository) {
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

    @Override
    public boolean isServiceIdExists(Long serviceId) {
        return serviceRepository.existsById(serviceId);
    }

    @Override
    @Transactional
    public void softDeleteServices(List<Long> ids) {
        serviceRepository.softDeleteServices(ids);
    }
}
