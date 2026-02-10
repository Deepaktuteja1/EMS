package com.example.employee_management_system.service.impl;

import com.example.employee_management_system.entity.Employee;
import com.example.employee_management_system.repository.EmployeeRepository;
import com.example.employee_management_system.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        // optional: prevent duplicate email
        employeeRepository.findByEmail(employee.getEmail())
                .ifPresent(e -> { throw new RuntimeException("Email already exists: " + employee.getEmail()); });

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existing = getEmployeeById(id);

        // optional: email duplicate check (only if email is changing)
        if (employee.getEmail() != null && !employee.getEmail().equals(existing.getEmail())) {
            employeeRepository.findByEmail(employee.getEmail())
                    .ifPresent(e -> { throw new RuntimeException("Email already exists: " + employee.getEmail()); });
        }

        existing.setName(employee.getName());
        existing.setEmail(employee.getEmail());
        existing.setDepartment(employee.getDepartment());

        return employeeRepository.save(existing);
    }

    @Override
    public void deleteEmployee(Long id) {
        // ensures proper error if not found
        getEmployeeById(id);
        employeeRepository.deleteById(id);
    }
}
