package com.example.employeemanagement.controller;

import java.util.List;
import com.example.employeemanagement.model.WorkSchedule;
import com.example.employeemanagement.repository.WorkScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;  
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;

@RestController
@RequestMapping("/api/workschedules")
public class WorkScheduleController {

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @GetMapping
    public List<WorkSchedule> getAll() {
        return workScheduleRepository.findAll();
    }

    @GetMapping("/employee/{id}")
    public List<WorkSchedule> getByEmployee(@PathVariable Long id) {
        return workScheduleRepository.findByEmployeeId(id);
    }

    @PostMapping
    public WorkSchedule create(@RequestBody WorkSchedule schedule) {
        return workScheduleRepository.save(schedule);
    }
    @GetMapping("/{id}")
    public ResponseEntity<WorkSchedule> getById(@PathVariable Long id) {
        Optional<WorkSchedule> schedule = workScheduleRepository.findById(id);
        return schedule.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public WorkSchedule update(@PathVariable Long id, @RequestBody WorkSchedule schedule) {
        schedule.setId(id);
        return workScheduleRepository.save(schedule);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        workScheduleRepository.deleteById(id);
    }

}