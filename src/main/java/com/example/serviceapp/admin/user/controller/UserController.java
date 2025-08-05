package com.example.serviceapp.admin.user.controller;

import com.example.serviceapp.admin.authenticate.repository.RoleRepository;
import com.example.serviceapp.admin.user.service.UserService;
import com.example.serviceapp.common.entity.Role;
import com.example.serviceapp.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String updateUser(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        User user = new User();
        user.setUserId(id);
        user.setFullName((String) payload.get("fullName"));
        user.setUsername((String) payload.get("username"));
        user.setEmail((String) payload.get("email"));
        user.setPhoneNumber((String) payload.get("phoneNumber"));
        user.setAddress((String) payload.get("address"));

        Long roleId = Long.valueOf(payload.get("roleId").toString());
        Role role = new Role();
        role.setRoleId(roleId);
        user.setRole(role);

        userService.updateUser(user);
        return "success";
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "success";
    }
}
