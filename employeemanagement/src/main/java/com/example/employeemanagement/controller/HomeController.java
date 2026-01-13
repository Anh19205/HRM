package com.example.employeemanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Landingpage"; // templates/index.html
    }
     @GetMapping("/employees")
    public String employeeList() {
        return "EmployeeList";   // -> templates/EmployeeList.html
    }
    @GetMapping("/schedule")
    public String schedule() {
        return "work-schedules";   // -> templates/Schedule.html
    }
    @GetMapping("/contracts")  
    public String contractList() {
        return "Contracts";   // -> templates/ContractList.html
    }

    @GetMapping("/employee-form")
    public String employeeForm() {
        return "EmployeeForm";   // -> templates/EmployeeForm.html
    }
    @GetMapping("/Admin")
    public String admin() {
        return "Admin"; // templates/Admin.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Dashboard"; // templates/dashboard.html
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // templates/register.html
    }
    

    @GetMapping("/verifyUsername")
    public String verifyUsername() {
        return "verifyUsername"; // templates/verify-username.html
    }

    @GetMapping("/resetPassword")
    public String resetPassword() {
        return "resetPassword"; // templates/reset-password.html
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile"; // templates/profile.html
    }
    @GetMapping("/departments")
    public String departments() {
        return "DepartmentList"; // templates/departments.html
    }
    @GetMapping("/department-form")
    public String departmentForm() {
        return "DepartmentForm";   // -> templates/DepartmentForm.html
    }
}
