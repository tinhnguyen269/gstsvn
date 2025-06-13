package com.example.serviceapp.admin.service.service.impl;

import com.example.serviceapp.admin.service.service.ServiceService;
import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.common.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService {


    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public Optional<Services> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public Services createService(Services services) {
        services.setCreateAt(LocalDateTime.now());
        services.setDeleteFlag(0);
        return serviceRepository.save(services);
    }

    @Override
    public Services updateService(Long id, Services updatedServices) {
        Optional<Services> optionalService = serviceRepository.findById(id);
        if (optionalService.isPresent()) {
            Services existingServices = optionalService.get();
            existingServices.setServiceName(updatedServices.getServiceName());
            existingServices.setDescription(updatedServices.getDescription());
            existingServices.setImgPrice(updatedServices.getImgPrice());
            existingServices.setUpdateAt(LocalDateTime.now());
            existingServices.setUpdateBy(updatedServices.getUpdateBy());
            return serviceRepository.save(existingServices);
        } else {
            throw new RuntimeException("Service not found with id: " + id);
        }
    }

    @Override
    public void deleteService(Long id) {
        Optional<Services> optionalService = serviceRepository.findById(id);
        if (optionalService.isPresent()) {
            Services services = optionalService.get();
            services.setDeleteFlag(1);
            services.setUpdateAt(LocalDateTime.now());
            serviceRepository.save(services);
        } else {
            throw new RuntimeException("Service not found with id: " + id);
        }
    }
}
