package com.example.serviceapp.customer.home.service.impl;

import com.example.serviceapp.common.repository.ServiceRepository;
import com.example.serviceapp.customer.home.service.HoneService;
import com.example.serviceapp.common.entity.Services;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HoneService {

    private final ServiceRepository serviceRepository;

    public HomeServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<Services> getAll() {
        return serviceRepository.findAll();
    }
}
