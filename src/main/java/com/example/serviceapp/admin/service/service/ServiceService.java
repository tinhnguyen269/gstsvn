package com.example.serviceapp.admin.service.service;

import com.example.serviceapp.common.entity.Services;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<Services> getAllServices();
    Optional<Services> getServiceById(Long id);
    Services createService(Services services);
    Services updateService(Long id, Services services);
    void deleteService(Long id);
}
