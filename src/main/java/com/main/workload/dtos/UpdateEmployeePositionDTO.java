package com.main.workload.dtos;

import com.main.workload.entities.EmployeePosition;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateEmployeePositionDTO {
    private Long id;
    @Nullable
    private Double rate;
    @Nullable
    private EmployeePosition.Post post;
    @Nullable
    private EmployeePosition.StructuralDivision structuralDivision;
    @Nullable
    private Boolean active;

}
