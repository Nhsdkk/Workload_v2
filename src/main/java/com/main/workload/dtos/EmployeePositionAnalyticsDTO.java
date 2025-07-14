package com.main.workload.dtos;

import com.main.workload.entities.EmployeePosition;
import lombok.Data;

@Data
public class EmployeePositionAnalyticsDTO {
    private Long id;
    private String name;
    private String typeOfEmployment;
    private String post;
    private String structuralDivision;
    private Double rate;
    private Boolean active;
    private Integer totalHours;
    private Boolean overloaded;

    public EmployeePositionAnalyticsDTO(EmployeePosition position) {
        setId(position.getId());
        setName(position.getEmployee().getName());
        setTypeOfEmployment(position.getEmployee().getTypeOfEmployment());
        setPost(position.getPost().toString());
        setRate(position.getRate());
        setActive(position.getActive());
        setOverloaded(position.isOverloaded());
        setTotalHours(position.getTotalWorkload());
        setStructuralDivision(position.getStructuralDivision().toString());
    }
}
