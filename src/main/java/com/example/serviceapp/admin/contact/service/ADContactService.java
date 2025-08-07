package com.example.serviceapp.admin.contact.service;

import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.common.entity.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ADContactService {
    void addCustomer(Customer customer);

    Page<Customer> searchCustomer(String keyword, Pageable pageable);

    Page<Customer> findAll(Pageable pageable);

    Customer findById(Long id);

    void save(Customer existing);

    void delete(Customer customer);

    List<Services> getAllServices();

    boolean isPhoneNumberExists(String phoneNumber);

}
