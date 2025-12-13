package com.example.serviceapp.admin.authenticate.service;

import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.common.entity.Role;
import com.example.serviceapp.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Tìm user theo username hoặc số điện thoại
        User user = userRepository.findByUsernameOrPhoneNumber(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với tên đăng nhập hoặc số điện thoại: " + identifier));
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                mapRoleToAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Role role) {
        if (role == null) {
            return Set.of(new SimpleGrantedAuthority("ROLE_GUEST"));
        }
        return Set.of(new SimpleGrantedAuthority(role.getRoleName()));
    }


}

