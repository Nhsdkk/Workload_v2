package com.main.workload.dtos;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
public class CreateWorkloadDTO {
    private List<String> workloadType;
    private Boolean active;
    private List<Long> studentGroupId;
    private Long lessonId;
    @Nullable
    private Long positionId;
}
