package com.example.serviceapp.admin.authenticate.repository;

import com.example.serviceapp.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deleteFlag = 0 ORDER BY u.userId ASC")
    List<User> findAllByUsername(@Param("username") String username);
    
    default Optional<User> findByUsername(String username) {
        List<User> users = findAllByUsername(username);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.deleteFlag = 0 ORDER BY u.userId ASC")
    List<User> findAllByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    default Optional<User> findByPhoneNumber(String phoneNumber) {
        List<User> users = findAllByPhoneNumber(phoneNumber);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Query("SELECT u FROM User u WHERE (u.username = :identifier OR u.phoneNumber = :identifier) AND u.deleteFlag = 0 ORDER BY u.userId ASC")
    List<User> findAllByUsernameOrPhoneNumber(@Param("identifier") String identifier);
    
    default Optional<User> findByUsernameOrPhoneNumber(String identifier) {
        List<User> users = findAllByUsernameOrPhoneNumber(identifier);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleteFlag = 0 ORDER BY u.userId ASC")
    List<User> findAllByEmail(@Param("email")String email);
    
    default Optional<User> findByEmail(String email) {
        List<User> users = findAllByEmail(email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    Optional<User> findByActivationCode(String activationCode);

    Optional<User> findByResetPasswordToken(String token);

    @Query("SELECT u FROM User u WHERE u.deleteFlag = 0 AND u.enabled = true AND LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> findByFullNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.deleteFlag = 1 WHERE u.userId = :id")
    void deleteUserById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.userId = :id AND u.deleteFlag = 0")
    Optional<User> findUserByIdById(Long id);

    @Query("SELECT u FROM User u WHERE u.deleteFlag = 0 ")
    Page<User> findAllUser(Pageable pageable);

}
