package com.example.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import javax.persistence.*;
import java.util.List;  
    

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private Long id;   // ← KHÔNG dùng @GeneratedValue nữa

    @MapsId               // ← Quan trọng nhất: dùng id của User làm id của chính Employee
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")   // hoặc giữ "user_id" nếu bạn muốn tên cột khác
    private User user;

    // Các field khác giữ nguyên
    private String firstName;
    private String lastName;
    private String email;
    private int age;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
 @JsonIgnoreProperties({"employee"})
    private List<WorkSchedule> workSchedules = new ArrayList<>();

    // ... getter/setter nếu không dùng Lombok @Data
}