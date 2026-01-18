package com.example.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;  
    

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

  /** The ID of the employee. It is unique and generated automatically. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The first name of the employee. */
  private String firstName;

  /** The last name of the employee. */
  private String lastName;

  /** The email of the employee. */
  private String email;

  /** The department of the employee. */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "department_id", nullable = false)
  
  private Department department;

  /** The age of the employee. */
  private int age;
  @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference
private List<WorkSchedule> workSchedules;

}
