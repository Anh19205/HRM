package com.example.employeemanagement.model;

import lombok.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_schedules")
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String shift; // e.g., "Morning", "Afternoon", "Night"
    @ManyToOne
@JoinColumn(name = "employee_id", nullable = false)
@JsonIgnoreProperties({"workSchedules"})
private Employee employee;

}