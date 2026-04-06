package com.example.omnianalytic.service;

import com.example.omnianalytic.model.Employee;
import com.example.omnianalytic.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void testSaveEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee saved = employeeService.saveEmployee(employee);

        assertNotNull(saved);
        assertEquals("John", saved.getFirstName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testSearchEmployees() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        Page<Employee> page = new PageImpl<>(Collections.singletonList(employee));
        Pageable pageable = PageRequest.of(0, 10);

        when(employeeRepository.findByFirstNameContainingOrLastNameContaining(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        Page<Employee> result = employeeService.searchEmployees("John", pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getFirstName());
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> found = employeeService.getEmployeeById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testCallExternalApi() {
        when(restTemplate.getForObject("https://www.google.com", String.class)).thenReturn("Google HTML");

        String response = employeeService.callExternalApi();

        assertEquals("Google HTML", response);
        verify(restTemplate, times(1)).getForObject("https://www.google.com", String.class);
    }
}
