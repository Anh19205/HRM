
package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Role;
import com.example.employeemanagement.model.User;
import com.example.employeemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.example.employeemanagement.model.ChangePasswordRequest;
import com.example.employeemanagement.model.CreateUserRequest;
import com.example.employeemanagement.model.UpdateUserRequest;  
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AdminController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /* ========= GET ALL ========= */
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /* ========= GET BY ID ========= */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ========= UPDATE ========= */
    @PutMapping("/{id}")
public ResponseEntity<?> updateUser(
        @PathVariable Long id,
        @RequestBody UpdateUserRequest request
) {
    return userRepository.findById(id).map(user -> {

        if (request.getUsername() != null)
            user.setUsername(request.getUsername());

        if (request.getRole() != null)
            user.setRole(request.getRole());

        if (request.getEnabled() != null)
            user.setEnabled(request.getEnabled());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User updated successfully"
        ));
    }).orElse(ResponseEntity.notFound().build());
}

    /* ========= DELETE ========= */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User deleted successfully"
        ));
    }
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {

    if (userRepository.existsByUsername(request.getUsername())) {
        return ResponseEntity.badRequest().body("Username đã tồn tại");
    }

    User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .enabled(request.getEnabled() != null ? request.getEnabled() : true)
            .build();

    userRepository.save(user);

    return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "User created successfully"
    ));
}
@PostMapping("/change-password")
public ResponseEntity<?> changeMyPassword(@RequestBody ChangePasswordRequest request) {
    if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
        return ResponseEntity.badRequest().body(Map.of(
            "success", false,
            "message", "Mật khẩu mới phải có ít nhất 6 ký tự"
        ));
    }

    if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
        return ResponseEntity.badRequest().body(Map.of(
            "success", false,
            "message", "Mật khẩu xác nhận không khớp"
        ));
    }

    // Lấy thông tin user hiện tại từ Security Context (JWT)
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = auth.getName();

    User user = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

    // Kiểm tra mật khẩu hiện tại có đúng không
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
        return ResponseEntity.badRequest().body(Map.of(
            "success", false,
            "message", "Mật khẩu hiện tại không đúng"
        ));
    }

    // Cập nhật mật khẩu mới
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    return ResponseEntity.ok(Map.of(
        "success", true,
        "message", "Đổi mật khẩu thành công"
    ));
}
}
