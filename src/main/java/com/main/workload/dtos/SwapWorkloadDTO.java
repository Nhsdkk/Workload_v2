package com.main.workload.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SwapWorkloadDTO {
    private List<Long> oldContainerIds;
    private List<Long> studentGroupsId;
    private Long newPositionId;
}
