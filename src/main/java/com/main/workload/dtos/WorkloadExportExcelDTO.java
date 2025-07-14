package com.main.workload.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkloadExportExcelDTO {
    private String lessonName;
    private String workloadTypes;
    private Integer workloadHours;
    private String teacherName;
    private String groups;
}
