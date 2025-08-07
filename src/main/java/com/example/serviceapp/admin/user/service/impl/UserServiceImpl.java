package com.example.serviceapp.admin.user.service.impl;

import com.example.serviceapp.admin.authenticate.repository.RoleRepository;
import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.admin.user.service.UserService;
import com.example.serviceapp.common.entity.Role;
import com.example.serviceapp.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<User> getUsers(String keyword, int page, int size) {
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByFullNameContainingIgnoreCase(keyword, PageRequest.of(page, size));
        }
        return userRepository.findAllUser(PageRequest.of(page, size));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findUserByIdById(id).orElse(null);
    }

    @Override
    public void updateUser(User updatedUser) {
        Optional<User> optionalUser = userRepository.findUserByIdById(updatedUser.getUserId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFullName(updatedUser.getFullName());
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setAddress(updatedUser.getAddress());

            if (updatedUser.getRole() != null && updatedUser.getRole().getRoleId() != null) {
                Role role = roleRepository.findById(updatedUser.getRole().getRoleId()).orElse(null);
                user.setRole(role);
            }

            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUserById(id);
    }
}
