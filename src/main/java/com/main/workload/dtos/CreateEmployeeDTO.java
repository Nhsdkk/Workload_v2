package com.main.workload.dtos;

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
        private String post;
        private String structuralDivision;
        private Double rate;
        private Boolean active;
    }
}
