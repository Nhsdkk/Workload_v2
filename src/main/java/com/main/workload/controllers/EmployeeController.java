package com.main.workload.controllers;

import com.main.workload.dtos.CreateEmployeeDTO;
import com.main.workload.dtos.EmployeeWithPositionsDTO;
import com.main.workload.dtos.UpdateEmployeeDTO;
import com.main.workload.services.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@Tag(name="Сотрудники", description = "API для управления сотрудниками")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public EmployeeWithPositionsDTO CreateEmployee(@RequestBody CreateEmployeeDTO dto) {
        return employeeService.createEmployee(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @PutMapping("/employee")
    public EmployeeWithPositionsDTO updateEmployee(@RequestBody UpdateEmployeeDTO dto) {
        return employeeService.updateEmployee(dto);
    }
}
