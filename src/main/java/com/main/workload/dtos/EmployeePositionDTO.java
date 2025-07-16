package com.main.workload.dtos;

import com.main.workload.entities.EmployeePosition;
import lombok.Data;

@Data
public class EmployeePositionDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String post;
    private Double rate;
    private String structuralDivision;
    private Boolean active;

    public EmployeePositionDTO(EmployeePosition position) {
        id = position.getId();
        employeeId = position.getEmployee().getId();
        employeeName = position.getEmployee().getName();
        post = position.getPost().getDisplayName();
        rate = position.getRate();
        structuralDivision = position.getStructuralDivision().getDisplayName();
        active = position.getActive();
    }
}