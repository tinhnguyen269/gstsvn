package com.example.serviceapp.customer.contact.service.impl;

import com.example.serviceapp.admin.contact.repository.ADContactRepository;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.customer.contact.service.ContactService;
import com.example.serviceapp.customer.service.repository.ServiceRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    private final ADContactRepository contactRepository;
    private final ServiceRepository serviceRepository;

    public ContactServiceImpl(ADContactRepository contactRepository, ServiceRepository serviceRepository) {
        this.contactRepository = contactRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void addCustomer(Customer customer) {
        contactRepository.save(customer);
    }

    @Override
    public boolean isServiceIdExists(Long serviceId) {
        return serviceRepository.existsById(serviceId);
    }

    @Override
    public boolean isPhoneNumberExists(String phoneNumber) {
        return contactRepository.isPhoneNumberExists(phoneNumber);
    }
}
