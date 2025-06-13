
package com.example.serviceapp.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Data
@Where(clause = "delete_flag = 0")
public class ImgService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgServiceId;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String imageServiceUrl;
    private LocalDateTime createAt;
    private Long createBy;
    private LocalDateTime updateAt;
    private Long updateBy;
    private int deleteFlag;
}
