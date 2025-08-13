package com.example.employeeapi.service;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import com.example.employeeapi.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee sample;

    @BeforeEach
    void setUp() {
        sample = new Employee("Alice", "Engineering", 95000.0);
        sample.setId(1L);
    }

    @Test
    void findAll_returnsEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(sample));

        List<Employee> result = employeeService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alice");
        verify(employeeRepository).findAll();
    }

    @Test
    void findById_found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sample));

        Optional<Employee> result = employeeService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getDepartment()).isEqualTo("Engineering");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void create_savesEmployee() {
        Employee toCreate = new Employee("Bob", "Sales", 70000.0);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            e.setId(2L);
            return e;
        });

        Employee saved = employeeService.create(toCreate);

        assertThat(saved.getId()).isEqualTo(2L);
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Bob");
    }

    @Test
    void update_whenExists_updatesAndReturns() {
        Employee changes = new Employee("Alice Smith", "Engineering", 99000.0);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Employee> updated = employeeService.update(1L, changes);

        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Alice Smith");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void update_whenMissing_returnsEmpty() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> updated = employeeService.update(99L, sample);

        assertThat(updated).isNotPresent();
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void delete_whenExists_returnsTrue() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        boolean removed = employeeService.delete(1L);

        assertThat(removed).isTrue();
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void delete_whenMissing_returnsFalse() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        boolean removed = employeeService.delete(1L);

        assertThat(removed).isFalse();
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}


