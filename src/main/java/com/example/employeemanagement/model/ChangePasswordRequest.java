
package com.example.employeemanagement.model;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String currentPassword;   // mật khẩu hiện tại
    private String newPassword;       // mật khẩu mới
    private String confirmNewPassword; // xác nhận mật khẩu mới
}