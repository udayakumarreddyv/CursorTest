package com.example.employeeapi.controller;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @WithMockUser(roles = {"USER"})
    void list_returnsOk() throws Exception {
        Mockito.when(employeeService.findAll()).thenReturn(Arrays.asList(
                new Employee("Alice", "Engineering", 95000.0)
        ));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void get_found_returnsEmployee() throws Exception {
        Employee alice = new Employee("Alice", "Engineering", 95000.0);
        alice.setId(1L);
        Mockito.when(employeeService.findById(1L)).thenReturn(Optional.of(alice));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void create_returnsCreated() throws Exception {
        Employee saved = new Employee("Bob", "Sales", 70000.0);
        saved.setId(2L);
        Mockito.when(employeeService.create(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bob\",\"department\":\"Sales\",\"salary\":70000}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/employees/2"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void update_whenFound_returnsOk() throws Exception {
        Employee updated = new Employee("Alice Smith", "Engineering", 99000.0);
        updated.setId(1L);
        Mockito.when(employeeService.update(eq(1L), any(Employee.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice Smith\",\"department\":\"Engineering\",\"salary\":99000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void delete_whenFound_returnsNoContent() throws Exception {
        Mockito.when(employeeService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }
}


