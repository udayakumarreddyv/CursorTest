package com.example.employeeapi.service;

import com.example.employeeapi.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    Employee create(Employee employee);
    Optional<Employee> update(Long id, Employee changes);
    boolean delete(Long id);
}


