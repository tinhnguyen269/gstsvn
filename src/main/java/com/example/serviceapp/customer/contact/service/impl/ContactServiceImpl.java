package com.example.serviceapp.customer.contact.service.impl;

import com.example.serviceapp.admin.contact.repository.ADContactRepository;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.customer.contact.service.ContactService;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    private final ADContactRepository contactRepository;

    public ContactServiceImpl(ADContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void addCustomer(Customer customer) {
        contactRepository.save(customer);
    }
}
