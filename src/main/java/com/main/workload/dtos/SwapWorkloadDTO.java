package com.main.workload.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SwapWorkloadDTO {
    private List<Long> oldContainerIds;
    private String workloadType;
    private List<Long> studentGroupsId;
    private Long lessonId;
    private Long newPositionId;
}
