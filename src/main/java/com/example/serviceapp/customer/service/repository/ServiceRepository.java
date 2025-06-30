package com.example.serviceapp.customer.service.repository;

import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

}
