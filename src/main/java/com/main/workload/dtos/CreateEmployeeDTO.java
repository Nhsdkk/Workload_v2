package com.main.workload.dtos;

import lombok.Data;

@Data
public class CreateEmployeeDTO {
    private String name;
    private String typeOfEmployment;
    private CreatePositionDTO position;

    @Data
    public static class CreatePositionDTO {
        private String post;
        private String structuralDivision;
        private Double rate;
        private Boolean active;
    }
}
