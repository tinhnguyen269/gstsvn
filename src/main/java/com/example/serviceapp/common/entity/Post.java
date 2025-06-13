
package com.example.serviceapp.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Data
@Where(clause = "delete_flag = 0")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;

    private LocalDateTime createdAt;
    private Long createBy;
    private LocalDateTime updateAt;
    private Long updateBy;
    private int deleteFlag;
}
