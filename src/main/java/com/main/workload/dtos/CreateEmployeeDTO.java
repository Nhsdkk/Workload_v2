package com.main.workload.dtos;

import com.main.workload.entities.EmployeePosition;
import lombok.Data;

import java.util.List;

@Data
public class CreateEmployeeDTO {
    private String name;
    private String typeOfEmployment;
    private CreatePositionDTO position;
    private List<Long> lessonIds;

    @Data
    public static class CreatePositionDTO {
        private EmployeePosition.Post post;
        private EmployeePosition.StructuralDivision structuralDivision;
        private Double rate;
        private Boolean active;
    }
}
