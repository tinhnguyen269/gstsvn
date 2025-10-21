package com.example.serviceapp.customer.contact.service;

import com.example.serviceapp.common.entity.Customer;

public interface ContactService {
    void addCustomer(Customer customer);

    boolean isServiceIdExists(Long serviceId);

    boolean isPhoneNumberExists(String phoneNumber);

    void sendNotificationToCompany(Customer customer);
}
