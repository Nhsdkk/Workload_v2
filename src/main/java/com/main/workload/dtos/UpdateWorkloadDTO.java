package com.main.workload.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;
import java.util.List;

@Data
public class UpdateWorkloadDTO {
    private List<Long> containerIds;
    @Nullable
    private Long lessonId;
    @Nullable
    private Long employeePositionId;
    @Nullable
    private List<Long> studentGroupId;
}
