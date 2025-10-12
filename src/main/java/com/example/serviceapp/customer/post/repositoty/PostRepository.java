package com.example.serviceapp.customer.post.repositoty;

import com.example.serviceapp.common.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.deleteFlag = 0 ORDER BY p.createAt DESC LIMIT 10")
    List<Post> findTop10NewestPosts();

    Optional<Post> findBySlug(String slug);
}

