package com.main.workload.services;

import com.main.workload.dtos.*;
import com.main.workload.entities.Employee;
import com.main.workload.entities.EmployeePosition;
import com.main.workload.exceptions.ResourceNotFoundException;
import com.main.workload.repositories.EmployeePositionRepository;
import com.main.workload.repositories.EmployeeRepository;
import com.main.workload.repositories.LessonRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeePositionRepository employeePositionRepository;
    private final LessonRepository lessonRepository;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeePositionRepository employeePositionRepository, LessonRepository lessonRepository) {
        this.employeeRepository = employeeRepository;
        this.employeePositionRepository = employeePositionRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<Employee> getEmployeesWithoutActivePositions() {
        List<Employee> res = employeeRepository.findByPositionsActiveFalse();
        res.addAll(employeeRepository.findByPositionsIsEmpty());
        return res;
    }

    public List<EmployeePosition> getPositionsWithoutCompetencesByStructuralDivision(@NonNull EmployeePosition.StructuralDivision division) {
        return employeePositionRepository.findAllByStructuralDivision(division).stream()
                .filter(x -> x.getEmployee().getAvailableLessons().isEmpty())
                .toList();
    }


    public List<EmployeePositionDTO> getAllEmployeePositions() {
        List<EmployeePosition> positions = employeePositionRepository.findAll();
        return positions.stream().map(EmployeePositionDTO::new).collect(Collectors.toList());
    }

    public EmployeePositionDetailDTO getEmployeePositionDetailById(Long id) {
        EmployeePosition position = employeePositionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        Employee employee = position.getEmployee();

        return new EmployeePositionDetailDTO(employee, position);
    }

    public EmployeePositionDetailDTO modifyEmployeeLessons(Long positionId, List<Long> lessonIds) {
        EmployeePosition position = employeePositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        Employee employee = position.getEmployee();
        var lessons = lessonRepository.findAllById(lessonIds);
        if (lessons.size() != lessonIds.size()) {
            throw new ResourceNotFoundException("Some lessons not found");
        }
        employee.setAvailableLessons(lessons);
        employeeRepository.save(employee);

        return new EmployeePositionDetailDTO(employee, position);
    }

    public EmployeeWithPositionsDTO createEmployee(CreateEmployeeDTO employeeDTO) {
        var employee = new Employee(employeeDTO);
        var lessons = lessonRepository.findAllById(employeeDTO.getLessonIds());
        if (lessons.size() != employeeDTO.getLessonIds().size()) {
            throw new ResourceNotFoundException("Some lessons not found");
        }
        employee.setAvailableLessons(lessons);
        employeeRepository.save(employee);
        return new EmployeeWithPositionsDTO(employee);
    }

    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    public EmployeePositionDetailDTO updateEmployeePosition(UpdateEmployeePositionDTO updateEmployeePositionDTO) {
        var position = employeePositionRepository.findById(updateEmployeePositionDTO.getId());
        if (position.isEmpty()) {
            throw new ResolutionException("Employee not found");
        }

        position.get().Update(updateEmployeePositionDTO);
        employeePositionRepository.save(position.get());
        return new EmployeePositionDetailDTO(position.get().getEmployee(), position.get());
    }

    public EmployeeWithPositionsDTO updateEmployee(UpdateEmployeeDTO updateEmployeeDTO) {
        var result = employeeRepository.findById(updateEmployeeDTO.getId());
        if (result.isEmpty()) {
            throw new ResolutionException("Employee not found");
        }

        result.get().Update(updateEmployeeDTO);
        employeeRepository.save(result.get());
        return new EmployeeWithPositionsDTO(result.get());
    }

    public EmployeeWithPositionsDTO createPosition(Long employeeId, CreateEmployeePositionDTO employeePositionDTO) {
        var employee = employeeRepository.findById(employeeId);
        if (employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee not found");
        }
        var position = new EmployeePosition(employeePositionDTO);
        employee.get().addPosition(position);
        employeeRepository.save(employee.get());
        return new EmployeeWithPositionsDTO(employee.get());
    }

    public List<EmployeeDTO> getEmployees() {
        return employeeRepository
                .findAll()
                .stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    public EmployeeWithPositionsDTO getEmployee(Long employeeId) {
        var employee = employeeRepository.findById(employeeId);
        if (employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee not found");
        }
        return new EmployeeWithPositionsDTO(employee.get());
    }

    public EmployeeWithPositionsDTO deletePosition(Long employeeId, Long positionId) {
        var position = employeePositionRepository.findById(employeeId);
        if (position.isEmpty()) {
            throw new ResourceNotFoundException("Employee position not found");
        }

        if (!Objects.equals(position.get().getEmployee().getId(), employeeId)) {
            throw new ResourceNotFoundException("Employee not found");
        }

        var employee = position.get().getEmployee();
        employeePositionRepository.deleteById(positionId);
        return new EmployeeWithPositionsDTO(employee);
    }
}