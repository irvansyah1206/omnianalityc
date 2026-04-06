package com.example.omnianalytic.service;

import com.example.omnianalytic.model.Employee;
import com.example.omnianalytic.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RestTemplate restTemplate;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Page<Employee> searchEmployees(String query, Pageable pageable) {
        return employeeRepository.findByFirstNameContainingOrLastNameContaining(query, query, pageable);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> getEmployeesByDeptName(String departmentName) {
        return employeeRepository.findEmployeesByDepartmentName(departmentName);
    }

    // Call external API
    public String callExternalApi() {
        log.info("Calling external API...");
        try {
            return restTemplate.getForObject("https://www.google.com", String.class);
        } catch (Exception e) {
            log.error("Failed to call external API: {}", e.getMessage());
            return "Error calling external API";
        }
    }
}
