package com.example.employeemanagement.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity đại diện cho tài khoản người dùng trong hệ thống
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password"}) // quan trọng: không in password ra log
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username không được để trống")
    @Size(min = 4, max = 50, message = "Username phải từ 4-50 ký tự")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Password không được để trống")
    @Column(nullable = false, length = 255) 
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private boolean enabled = true;

    // Các trường bổ sung rất hữu ích cho thực tế
    @Column(length = 100)
    private String fullName;           // Họ tên đầy đủ (đồng bộ với Employee)

    @Column(length = 100)
    private String email;              // email (có thể đồng bộ với Employee)

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    // Quan hệ 1-1 với Employee (nếu cần truy vấn ngược)
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Employee employee;

    // Helper method tiện lợi
    public boolean isAdmin() {
        return Role.ADMIN.equals(this.role);
    }

    public boolean isEmployee() {
        return Role.EMPLOYEE.equals(this.role);
    }
    
}