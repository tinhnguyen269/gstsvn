package com.example.serviceapp.customer.contact.service;

import com.example.serviceapp.common.entity.Customer;

public interface ContactService {
    void addCustomer(Customer customer);

    boolean isServiceIdExists(Long serviceId);

    void sendNotificationToCompany(Customer customer);
}
