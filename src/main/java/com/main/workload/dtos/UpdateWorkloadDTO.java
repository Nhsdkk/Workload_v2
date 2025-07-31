package com.main.workload.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateWorkloadDTO {
    private Long id;
    @Nullable
    private Long lessonId;
    @Nullable
    private String workloadType;
    @Nullable
    private Long employeePositionId;
    @Nullable
    private Long studentGroupId;
}
