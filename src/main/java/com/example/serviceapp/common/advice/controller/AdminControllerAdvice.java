package com.example.serviceapp.common.advice.controller;

import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.common.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.example.serviceapp.admin")
public class AdminControllerAdvice {
    
    private final UserRepository userRepository;
    
    public AdminControllerAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @ModelAttribute
    public void addCurrentURI(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
    }

    @ModelAttribute
    public void addUserRole(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        String userFullName = null;
        String username = null;
        
        if (authentication != null && authentication.isAuthenticated() 
            && !authentication.getName().equals("anonymousUser")) {
            username = authentication.getName();
            isAdmin = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
            
            // Lấy thông tin user từ database (có thể là username hoặc phoneNumber)
            try {
                User user = userRepository.findByUsernameOrPhoneNumber(username).orElse(null);
                if (user != null) {
                    userFullName = user.getFullName();
                }
            } catch (Exception e) {
                // Nếu không tìm thấy, giữ null
            }
        }
        
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("currentUserFullName", userFullName);
        model.addAttribute("currentUsername", username);
    }
}