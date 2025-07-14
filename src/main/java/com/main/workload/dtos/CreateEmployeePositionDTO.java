package com.main.workload.dtos;

import lombok.Data;

@Data
public class CreateEmployeePositionDTO {
    private String post;
    private String structuralDivision;
    private Double rate;
    private Boolean active;
}