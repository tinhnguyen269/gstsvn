package com.example.serviceapp.customer.contact.repository;

import com.example.serviceapp.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Customer,Long> {
}
