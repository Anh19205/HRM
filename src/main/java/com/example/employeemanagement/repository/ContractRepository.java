package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByEmployeeId(Long employeeId);  // trả List thay vì single
    Contract findTopByEmployeeIdOrderByStartDateDesc(Long employeeId);
}
