package com.main.workload.controllers;

import com.main.workload.dtos.*;
import com.main.workload.services.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-positions")
@Tag(name = "Позиции преподавателей", description = "API для управления позициями преподавателей")

public class EmployeePositionController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<EmployeePositionDTO> getAllEmployeePositions() {
        return employeeService.getAllEmployeePositions();
    }

    @GetMapping("/{id}")
    public EmployeePositionDetailDTO getEmployeePositionDetail(@PathVariable Long id) {
        return employeeService.getEmployeePositionDetailById(id);
    }

    @PostMapping("/{id}/modify-lessons")
    public EmployeePositionDetailDTO modifyEmployeeLessons(@PathVariable Long id, @RequestParam List<Long> lessonIds) {
        return employeeService.modifyEmployeeLessons(id, lessonIds);
    }

    @PutMapping
    public EmployeePositionDetailDTO updateEmployeePosition(@RequestBody UpdateEmployeePositionDTO dto) {
        return employeeService.updateEmployeePosition(dto);
    }

    @PostMapping("/{id}")
    public EmployeeWithPositionsDTO addEmployeePosition(@PathVariable Long id, @RequestBody CreateEmployeePositionDTO dto) {
        return employeeService.createPosition(id, dto);
    }

    @DeleteMapping("/{employeeId}/{positionId}")
    public EmployeeWithPositionsDTO deleteEmployeePosition(@PathVariable Long employeeId, @PathVariable Long positionId) {
        return employeeService.deletePosition(employeeId, positionId);
    }
}

