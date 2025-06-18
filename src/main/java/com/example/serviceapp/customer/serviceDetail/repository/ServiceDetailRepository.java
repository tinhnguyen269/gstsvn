package com.example.serviceapp.customer.serviceDetail.repository;

import com.example.serviceapp.common.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceDetailRepository extends JpaRepository<Services, Long> {
}
