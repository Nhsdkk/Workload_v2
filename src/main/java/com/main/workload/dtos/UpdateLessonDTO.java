package com.main.workload.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateLessonDTO {
    private Long id;
    @Nullable
    private String name;
    @Nullable
    private Integer semester;
}
