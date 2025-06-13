
package com.example.serviceapp.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Data
@Where(clause = "delete_flag = 0")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;
    private String name;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(columnDefinition = "TEXT")
    private String message;
    private String status;
    private LocalDateTime createAt;
    private Long createBy;
    private LocalDateTime updateAt;
    private Long updateBy;
    private int deleteFlag;
}
