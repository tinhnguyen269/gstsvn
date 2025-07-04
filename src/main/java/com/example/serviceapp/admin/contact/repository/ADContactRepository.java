package com.example.serviceapp.admin.contact.repository;

import com.example.serviceapp.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ADContactRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c " +
            "WHERE (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR c.phoneNumber LIKE CONCAT('%', :keyword, '%')) " +
            "AND c.deleteFlag = 0 " +
            "ORDER BY c.createAt DESC")
    Page<Customer> searchCustomer(@Param("keyword") String keyword, Pageable pageable);

}
