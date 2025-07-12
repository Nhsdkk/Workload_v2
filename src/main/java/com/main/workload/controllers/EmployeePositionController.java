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

    @PostMapping("/{id}/add-lesson")
    public EmployeePositionDetailDTO addLessonToEmployee(@PathVariable Long id, @RequestParam Long lessonId) {
        return employeeService.addLessonToEmployee(id, lessonId);
    }

    @DeleteMapping("/{id}/remove-lesson/{lessonId}")
    public void removeLessonFromEmployee(@PathVariable Long id, @PathVariable Long lessonId) {
        employeeService.removeLessonFromEmployee(id, lessonId);
    }

    @PutMapping
    public EmployeePositionDetailDTO updateEmployeePosition(@RequestBody UpdateEmployeePositionDTO dto) {
        return employeeService.updateEmployeePosition(dto);
    }}