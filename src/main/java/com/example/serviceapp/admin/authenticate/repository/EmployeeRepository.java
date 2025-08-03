package com.example.serviceapp.admin.authenticate.repository;

import com.example.serviceapp.common.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}