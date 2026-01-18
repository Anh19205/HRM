package com.example.employeemanagement.service;

import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.model.*;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;           // thêm repository này
    private final PasswordEncoder passwordEncoder;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllWithDepartments();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
public Employee createEmployee(Employee employee) {
    // 1. Tạo và lưu User trước → lúc này có id
    User user = createUserAccountForEmployee(employee);

    // 2. Gán user và id (đây là bước quyết định shared PK)
    employee.setUser(user);
    employee.setId(user.getId());   // ← THÊM DÒNG NÀY

    // 3. Lưu employee (id đã được set, không sinh mới)
    return employeeRepository.save(employee);
}

    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Chỉ update các thông tin cơ bản (KHÔNG đụng đến User account)
        existing.setFirstName(employeeDetails.getFirstName());
        existing.setLastName(employeeDetails.getLastName());
        existing.setEmail(employeeDetails.getEmail());
        existing.setDepartment(employeeDetails.getDepartment());
        existing.setAge(employeeDetails.getAge());

        // Nếu sau này muốn cho phép đổi email -> username thì xử lý riêng ở đây

        return employeeRepository.save(existing);
    }

    @Transactional
public void deleteEmployee(Long id) {

    Employee employee = employeeRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Employee not found with id: " + id));

    User user = employee.getUser();

    // 1️⃣ Xóa employee
    employeeRepository.delete(employee);

    // 2️⃣ Xóa user tương ứng
    if (user != null) {
        userRepository.delete(user);
    }
}


    // ================== Helper methods ==================

    private User createUserAccountForEmployee(Employee employee) {
        String username = generateUsername(employee);

        // Check trùng username
       if (userRepository.findByUsername(username).isPresent()) {
        throw new IllegalArgumentException("Username đã tồn tại: " + username);
    }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("123456")); // default password
        user.setRole(Role.EMPLOYEE);
        user.setEnabled(true);
        user.setFullName(employee.getFirstName() + " " + employee.getLastName());

        // Lưu user trước để có ID
        return userRepository.save(user);
    }

    private String generateUsername(Employee employee) {
    if (employee.getFirstName() == null || employee.getLastName() == null) {
        throw new IllegalArgumentException("First name và Last name không được để trống");
    }

    String base = (employee.getFirstName().charAt(0) + employee.getLastName())
            .toLowerCase()
            .replaceAll("\\s+", "");

    String username = base;
    int count = 1;

    // Sửa phần check trùng
    while (userRepository.findByUsername(username).isPresent()) {
        username = base + count++;
    }

    return username;
}
}