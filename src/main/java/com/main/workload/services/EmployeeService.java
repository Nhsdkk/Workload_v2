package com.main.workload.services;

import com.main.workload.dtos.*;
import com.main.workload.entities.Employee;
import com.main.workload.entities.EmployeePosition;
import com.main.workload.entities.Lesson;
import com.main.workload.repositories.EmployeePositionRepository;
import com.main.workload.repositories.EmployeeRepository;
import com.main.workload.repositories.LessonRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
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

    public EmployeePositionDetailDTO addLessonToEmployee(Long positionId, Long lessonId) {
        EmployeePosition position = employeePositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        Employee employee = position.getEmployee();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        if (!employee.getAvailableLessons().contains(lesson)) {
            employee.addLesson(lesson);
            employeeRepository.save(employee);
        }

        return new EmployeePositionDetailDTO(employee, position);
    }

    public void removeLessonFromEmployee(Long positionId, Long lessonId) {
        EmployeePosition position = employeePositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        Employee employee = position.getEmployee();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        if (employee.getAvailableLessons().contains(lesson)) {
            employee.getAvailableLessons().remove(lesson);
            employeeRepository.save(employee);
        }
    }

    public EmployeeWithPositionsDTO createEmployee(CreateEmployeeDTO employeeDTO) {
        var employee = new Employee(employeeDTO);
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
}