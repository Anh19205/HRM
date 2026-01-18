package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.WorkSchedule;
import com.example.employeemanagement.model.User;
import com.example.employeemanagement.repository.WorkScheduleRepository;
import com.example.employeemanagement.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.Role;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/workschedules")
public class WorkScheduleController {
    @Autowired
private EmployeeRepository employeeRepository;

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @Autowired
    private UserRepository userRepository;

    // =============================================================
    // Xem tất cả lịch (ADMIN + DEPARTMENT)
    // =============================================================
    @GetMapping
    public List<WorkSchedule> getAll(Authentication authentication) {

        User user = getCurrentUser(authentication);

        if (!isAdminOrDepartment(user)) {
            throw new AccessDeniedException("Chỉ ADMIN hoặc DEPARTMENT được xem tất cả lịch làm việc");
        }

        return workScheduleRepository.findAll();
    }

    // =============================================================
    // Xem lịch của 1 nhân viên
    // =============================================================
 @GetMapping("/employee/{employeeId}")
public List<WorkSchedule> getByEmployee(
        @PathVariable Long employeeId,
        Authentication authentication) {

    UserDetailsImpl principal =
        (UserDetailsImpl) authentication.getPrincipal();

    Long userId = principal.getId();
    boolean isEmployee = principal.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

    // EMPLOYEE chỉ được xem lịch của chính mình
    if (isEmployee && !userId.equals(employeeId)) {
        throw new AccessDeniedException("Bạn chỉ được xem lịch của chính mình");
    }

    return workScheduleRepository.findByEmployeeId(employeeId);
}




    // =============================================================
    // Tạo lịch (ADMIN + DEPARTMENT)
    // =============================================================
    @PostMapping
    public WorkSchedule create(
            @RequestBody WorkSchedule schedule,
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        if (!isAdminOrDepartment(user)) {
            throw new AccessDeniedException("Chỉ ADMIN hoặc DEPARTMENT được tạo lịch làm việc");
        }

        return workScheduleRepository.save(schedule);
    }

    // =============================================================
    // Sửa lịch (ADMIN + DEPARTMENT)
    // =============================================================
    @PutMapping("/{id}")
    public WorkSchedule update(
            @PathVariable Long id,
            @RequestBody WorkSchedule schedule,
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        if (!isAdminOrDepartment(user)) {
            throw new AccessDeniedException("Chỉ ADMIN hoặc DEPARTMENT được sửa lịch làm việc");
        }

        if (!workScheduleRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy lịch làm việc với id: " + id);
        }

        schedule.setId(id);
        return workScheduleRepository.save(schedule);
    }

    // =============================================================
    // Xóa lịch (ADMIN + DEPARTMENT)
    // =============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        if (!isAdminOrDepartment(user)) {
            throw new AccessDeniedException("Chỉ ADMIN hoặc DEPARTMENT được xóa lịch làm việc");
        }

        if (!workScheduleRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy lịch làm việc với id: " + id);
        }

        workScheduleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // =============================================================
    // Helper
    // =============================================================
    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName(); // lấy từ JWT
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isAdminOrDepartment(User user) {
        return user.isAdmin() || Role.DEPARTMENT.equals(user.getRole());
    }
   @GetMapping("/my")
public List<WorkSchedule> mySchedule(Authentication authentication) {

    UserDetailsImpl principal =
        (UserDetailsImpl) authentication.getPrincipal();

    Long employeeId = principal.getId(); // userId == employeeId

    return workScheduleRepository.findByEmployeeId(employeeId);
}



}
