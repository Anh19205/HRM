package com.example.employeemanagement.config;

import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private EmployeeRepository employeeRepository;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        // Chỉ chạy khi cả hai bảng đều trống (tức là lần đầu tiên)
        if (departmentRepository.count() == 0 && employeeRepository.count() == 0) {
            System.out.println("Database trống - Đang sinh fake data lần đầu...");

            List<Department> departments = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {  // Giảm xuống 10 phòng ban thôi cho gọn
                Department department = new Department();
                department.setName(faker.company().industry());
                departments.add(department);
            }
            departmentRepository.saveAll(departments);

            List<Employee> employees = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {  // Giảm xuống 50 nhân viên
                Employee employee = new Employee();
                employee.setFirstName(faker.name().firstName());
                employee.setLastName(faker.name().lastName());
                employee.setEmail(faker.internet().emailAddress());
                employee.setAge(random.nextInt(40) + 20);

                employee.setDepartment(departments.get(random.nextInt(departments.size())));
                employees.add(employee);
            }
            employeeRepository.saveAll(employees);

            System.out.println("Đã sinh fake data thành công (chỉ lần đầu)!");
        } else {
            System.out.println("Database đã có dữ liệu - Không sinh lại fake data.");
        }
    }
}