package com.example.serviceapp.admin.contact.service.impl;

import com.example.serviceapp.admin.contact.repository.ADContactRepository;
import com.example.serviceapp.admin.contact.service.ADContactService;
import com.example.serviceapp.admin.service.repository.ADServiceRepository;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.common.entity.Services;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ADContactServiceImpl implements ADContactService {

    public final ADContactRepository contactRepository;
    public final ADServiceRepository serviceRepository;

    public ADContactServiceImpl(ADContactRepository contactRepository, ADServiceRepository serviceRepository) {
        this.contactRepository = contactRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void addCustomer(Customer customer) {
        contactRepository.save(customer);
    }

    @Override
    public Page<Customer> searchCustomer(String keyword, Pageable pageable) {
        return contactRepository.searchCustomer(keyword, pageable);
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    @Override
    public Customer findById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    public void save(Customer existing) {
        contactRepository.save(existing);
    }

    @Override
    public void delete(Customer customer) {
        customer.setDeleteFlag(1);
        contactRepository.save(customer);
    }

    @Override
    public List<Services> getAllServices() {
        return serviceRepository.getAllService();
    }

    @Override
    public boolean isPhoneNumberExists(String phoneNumber) {
        return contactRepository.isPhoneNumberExists(phoneNumber);
    }

    @Override
    public boolean isPhoneNumberUpdateExists(String phoneNumber, Long id) {
        return contactRepository.isPhoneNumberUpdateExists(phoneNumber, id);
    }

    @Override
    @Transactional
    public void softDeleteContacts(List<Long> ids) {
        contactRepository.softDeleteContacts(ids);
    }

}
