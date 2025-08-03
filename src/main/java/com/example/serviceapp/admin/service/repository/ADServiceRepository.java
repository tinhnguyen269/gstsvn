package com.example.serviceapp.admin.service.repository;

import com.example.serviceapp.common.entity.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ADServiceRepository extends JpaRepository<Services,Long> {
    @Query("SELECT s FROM Services s WHERE LOWER(s.serviceName) LIKE LOWER(CONCAT('%', :keyword, '%')) AND s.deleteFlag = 0 ORDER BY s.createAt DESC")
    Page<Services> searchServices(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM services WHERE delete_flag = 0 ORDER BY create_at DESC", nativeQuery = true)
    List<Services> getAllService();
}
