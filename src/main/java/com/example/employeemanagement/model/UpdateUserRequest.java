package com.example.employeemanagement.model;
import lombok.Data;
@Data
public class UpdateUserRequest {
    private String username;
    private String password; // optional
    private Role role;
    private Boolean enabled;
}
