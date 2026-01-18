package com.example.employeemanagement.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.employeemanagement.model.Contract;
import com.example.employeemanagement.repository.ContractRepository;
import com.example.employeemanagement.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;         
import org.springframework.web.bind.annotation.PutMapping;          
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

import java.util.HashMap;
import java.util.List;  

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "http://localhost:3000")
public class ContractController {

    @Autowired
    private ContractRepository contractRepository;

    // 1. Lấy tất cả hợp đồng
    @GetMapping
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    // 2. Lấy hợp đồng theo ID
    @GetMapping("/{id}")
    public Contract getContractById(@PathVariable Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found: " + id));
    }

    // 3. Lấy hợp đồng theo nhân viên
    @GetMapping("/employee/{employeeId}")
public List<Contract> getByEmployee(@PathVariable Long employeeId) {
    return contractRepository.findByEmployeeId(employeeId);
}

    // 4. Tạo hợp đồng
    @PostMapping
    public Contract create(@RequestBody Contract contract) {
        return contractRepository.save(contract);
    }

    // 5. Cập nhật hợp đồng
    @PutMapping("/{id}")
    public Contract update(@PathVariable Long id, @RequestBody Contract data) {
        Contract existing = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        existing.setContractType(data.getContractType());
        existing.setSalary(data.getSalary());
        existing.setStartDate(data.getStartDate());
        existing.setEndDate(data.getEndDate());

        return contractRepository.save(existing);
    }

    // 6. Xóa hợp đồng
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contractRepository.deleteById(id);
    }
    @GetMapping("/employee/{employeeId}/contract-type")
public Map<String, Object> getLatestContractType(
        @PathVariable Long employeeId) {

    Contract contract =
        contractRepository.findTopByEmployeeIdOrderByStartDateDesc(employeeId);

    Map<String, Object> response = new HashMap<>();
    response.put("employeeId", employeeId);
    response.put("contractType",
        contract != null ? contract.getContractType() : null);

    return response;
}
@GetMapping("/my")
public List<Contract> getMyContracts(Authentication authentication) {
    // 1. Lấy Principal từ SecurityContext
    UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
    
    // 2. Lấy ID của Employee (giả định ID User == ID Employee)
    Long employeeId = principal.getId();
    
    // 3. Trả về danh sách hợp đồng của nhân viên đó
    return contractRepository.findByEmployeeId(employeeId);
}
}
