package com.example.serviceapp.admin.contact.service;

import com.example.serviceapp.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {
    void addCustomer(Customer customer);

    Page<Customer> searchCustomer(String keyword, Pageable pageable);

    Page<Customer> findAll(Pageable pageable);

    Customer findById(Long id);

    void save(Customer existing);
}
