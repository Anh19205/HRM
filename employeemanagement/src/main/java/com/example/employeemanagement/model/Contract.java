package com.example.employeemanagement.model;

import lombok.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contractType; // Full-time, Part-time
    private Double salary;
    private LocalDate startDate;
    private LocalDate endDate;


@OneToOne
@JoinColumn(name = "employee_id")
private Employee employee;  // không cần thêm ignore gì nữa


}
