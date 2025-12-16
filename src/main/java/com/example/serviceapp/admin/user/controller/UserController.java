package com.example.serviceapp.admin.user.controller;

import com.example.serviceapp.admin.authenticate.repository.RoleRepository;
import com.example.serviceapp.admin.user.dto.UserUpdateDTO;
import com.example.serviceapp.admin.user.service.UserService;
import com.example.serviceapp.common.entity.Role;
import com.example.serviceapp.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final com.example.serviceapp.admin.authenticate.service.UserService userAuthenticateService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, UserService userAuthenticateService, com.example.serviceapp.admin.authenticate.service.UserService userAuthenticateService1, RoleRepository roleRepository) {
        this.userService = userService;
        this.userAuthenticateService = userAuthenticateService1;
        this.roleRepository = roleRepository;
    }


    @GetMapping("/list")
    public String listUsers(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword
    ) {
        Page<User> userPage = userService.getUsers(keyword, page, size);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("allRoles", roleRepository.findAll());

        return "admin/user/user";
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public Map<String, Object> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return Map.of("error", "Không tìm thấy người dùng!");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("fullName", user.getFullName());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("phoneNumber", user.getPhoneNumber());
        response.put("address", user.getAddress());
        response.put("roleId", user.getRole() != null ? user.getRole().getRoleId() : null);

        return response;
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody @Validated(User.OnUpdate.class) UserUpdateDTO userFromClient,
            BindingResult bindingResult) {

        User currentUser = userService.getUserById(id);
        if (currentUser == null) {
            return createErrorResponse("Không tìm thấy người dùng!");
        }

        // Validate duplicate fields (exclude current user)
        String duplicateError = validateDuplicateFields(
                userFromClient.getUsername(),
                userFromClient.getEmail(),
                userFromClient.getPhoneNumber(),
                id
        );
        if (duplicateError != null) {
            return createErrorResponse(duplicateError);
        }

        // Validate binding errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }

        // Convert DTO -> Entity
        User user = convertDtoToEntity(userFromClient, id);
        userService.updateUser(user);

        return ResponseEntity.ok("success");
    }

    /**
     * Validate duplicate username, email, phoneNumber
     * @param username username to check
     * @param email email to check
     * @param phoneNumber phoneNumber to check
     * @param excludeUserId user ID to exclude from check
     * @return error message if duplicate found, null otherwise
     */
    private String validateDuplicateFields(String username, String email, String phoneNumber, Long excludeUserId) {
        if (username != null) {
            Optional<User> existingUser = userAuthenticateService.findByUsername(username);
            if (existingUser.isPresent() && !existingUser.get().getUserId().equals(excludeUserId)) {
                return "Tên đăng nhập đã được sử dụng!";
            }
        }

        if (email != null) {
            Optional<User> existingUser = userAuthenticateService.findByEmail(email);
            if (existingUser.isPresent() && !existingUser.get().getUserId().equals(excludeUserId)) {
                return "Email đã được sử dụng!";
            }
        }

        if (phoneNumber != null) {
            Optional<User> existingUser = userAuthenticateService.findByPhoneNumber(phoneNumber);
            if (existingUser.isPresent() && !existingUser.get().getUserId().equals(excludeUserId)) {
                return "Số điện thoại đã được sử dụng!";
            }
        }

        return null;
    }

    /**
     * Create error response with message
     */
    private ResponseEntity<Map<String, String>> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("message", message);
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Convert UserUpdateDTO to User entity
     */
    private User convertDtoToEntity(UserUpdateDTO dto, Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());

        Role role = new Role();
        role.setRoleId(dto.getRoleId());
        user.setRole(role);

        return user;
    }


    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "User not found";
        }
        userService.deleteUser(id);
        return "success";
    }
}
