package com.example.serviceapp.admin.post.repository;

import com.example.serviceapp.common.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ADPostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.deleteFlag = 0 ORDER BY p.createAt DESC")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.deleteFlag = 0 ORDER BY p.createAt DESC limit 9")
    List<Post> findTop9ByOrderByCreateAtDesc();

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.deleteFlag = 1 WHERE p.postId IN :ids")
    void softDeletePosts(@Param("ids") List<Long> ids);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Post p WHERE LOWER(p.title) = LOWER(:title) AND p.deleteFlag = 0")
    boolean existsByTitle(@Param("title") String title);

}
