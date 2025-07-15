package com.main.workload.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.List;

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
    @Nullable
    private List<Long> competences;
}
