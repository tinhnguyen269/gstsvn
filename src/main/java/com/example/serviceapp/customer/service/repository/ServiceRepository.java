package com.example.serviceapp.customer.service.repository;

import com.example.serviceapp.common.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {
    @Query(value = "SELECT * FROM services WHERE delete_flag = 0 ORDER BY create_at DESC ", nativeQuery = true)
    List<Services> getAllService();

    Optional<Services> findBySlug(String slug);
}
