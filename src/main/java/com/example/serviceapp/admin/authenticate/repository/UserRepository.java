package com.example.serviceapp.admin.authenticate.repository;

import com.example.serviceapp.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deleteFlag = 0")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleteFlag = 0")
    Optional<User> findByEmail(@Param("email")String email);

    Optional<User> findByActivationCode(String activationCode);

    Optional<User> findByResetPasswordToken(String token);

    @Query("SELECT u FROM User u WHERE u.deleteFlag = 0 AND u.enabled = true AND LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> findByFullNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.deleteFlag = 1 WHERE u.userId = :id")
    void deleteUserById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.userId = :id AND u.deleteFlag = 0")
    Optional<User> findUserByIdById(Long id);

    @Query("SELECT u FROM User u WHERE u.deleteFlag = 0 AND u.enabled = true ")
    Page<User> findAllUser(Pageable pageable);

}
