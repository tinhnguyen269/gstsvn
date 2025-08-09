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

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
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

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }

        // Convert DTO -> Entity
        User user = new User();
        user.setUserId(id);
        user.setFullName(userFromClient.getFullName());
        user.setUsername(userFromClient.getUsername());
        user.setEmail(userFromClient.getEmail());
        user.setPhoneNumber(userFromClient.getPhoneNumber());
        user.setAddress(userFromClient.getAddress());

        Role role = new Role();
        role.setRoleId(userFromClient.getRoleId());
        user.setRole(role);

        userService.updateUser(user);

        return ResponseEntity.ok("success");
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
