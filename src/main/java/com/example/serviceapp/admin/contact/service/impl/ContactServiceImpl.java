package com.example.serviceapp.admin.contact.service.impl;

import com.example.serviceapp.admin.contact.repository.ContactRepository;
import com.example.serviceapp.admin.contact.service.ContactService;
import com.example.serviceapp.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    public final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
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

}
