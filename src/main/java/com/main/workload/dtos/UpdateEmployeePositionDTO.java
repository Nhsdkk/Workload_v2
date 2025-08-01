package com.main.workload.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateEmployeePositionDTO {
    private Long id;
    @Nullable
    private Double rate;
    @Nullable
    private String post;
    @Nullable
    private String structuralDivision;
    @Nullable
    private Boolean active;
}
