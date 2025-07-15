package com.main.workload.dtos;

import com.main.workload.entities.Employee;
import com.main.workload.entities.EmployeePosition;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EmployeePositionDetailDTO {
    private Long id;
    private String employeeName;
    private String post;
    private Double rate;
    private String structuralDivision;
    private Boolean active;
    private List<LessonDTO> competencies;

    public EmployeePositionDetailDTO(Employee employee, EmployeePosition position) {
        setId(position.getId());
        setEmployeeName(position.getEmployee().getName());
        setPost(position.getPost().getDisplayName());
        setRate(position.getRate());
        setStructuralDivision(position.getStructuralDivision().getDisplayName());
        setActive(position.getActive());
        setCompetencies(employee.getAvailableLessons().stream().map(LessonDTO::new).collect(Collectors.toList()));
    }
}