package com.example.employeemanagement.model;
import lombok.Data;
@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private Role role;
    private Boolean enabled;
}

