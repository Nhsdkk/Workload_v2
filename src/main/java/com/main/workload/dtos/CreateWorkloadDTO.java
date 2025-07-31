package com.main.workload.dtos;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class CreateWorkloadDTO {
    private String workloadType;
    private Boolean active;
    private Long studentGroupId;
    private Long LessonId;
    @Nullable
    private Long PositionId;
}
