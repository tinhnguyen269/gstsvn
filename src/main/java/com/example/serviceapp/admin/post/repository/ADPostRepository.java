package com.example.serviceapp.admin.post.repository;

import com.example.serviceapp.common.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ADPostRepository extends JpaRepository<Post, Long> {}
