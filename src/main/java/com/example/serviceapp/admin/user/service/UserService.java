package com.example.serviceapp.admin.user.service;

import com.example.serviceapp.common.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<User> getUsers(String keyword, int page, int size);
    User getUserById(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
}