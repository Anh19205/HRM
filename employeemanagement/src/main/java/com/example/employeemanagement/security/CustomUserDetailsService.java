package com.example.employeemanagement.security;

import com.example.employeemanagement.model.Role;          
import com.example.employeemanagement.model.User;
import com.example.employeemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Custom UserDetailsService để load user từ database
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Trong CustomUserDetailsService
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    System.out.println("Loaded user: " + username + " | Role: " + user.getRole());
    System.out.println("Authorities: " + getAuthorities(user.getRole()));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        user.isEnabled(),
        true, true, true,
        getAuthorities(user.getRole())
    );
}

    /**
     * Trả về danh sách quyền (authorities) dựa trên Role
     */
    private List<GrantedAuthority> getAuthorities(Role role) {
        // Cách đơn giản: mỗi user chỉ có 1 role
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }
}