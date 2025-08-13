package com.example.employeeapi.service.impl;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import com.example.employeeapi.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        log.debug("Fetching all employees");
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        log.debug("Fetching employee by id={}", id);
        return employeeRepository.findById(id);
    }

    @Override
    public Employee create(Employee employee) {
        log.info("Creating employee name={} department={}", employee.getName(), employee.getDepartment());
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> update(Long id, Employee changes) {
        return employeeRepository.findById(id).map(existing -> {
            existing.setName(changes.getName());
            existing.setDepartment(changes.getDepartment());
            existing.setSalary(changes.getSalary());
            log.info("Updated employee id={}", id);
            return employeeRepository.save(existing);
        });
    }

    @Override
    public boolean delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            return false;
        }
        employeeRepository.deleteById(id);
        log.warn("Deleted employee id={}", id);
        return true;
    }
}


