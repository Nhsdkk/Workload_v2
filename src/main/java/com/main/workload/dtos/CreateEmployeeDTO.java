package com.main.workload.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateEmployeeDTO {
    private String name;
    private String typeOfEmployment;
    private List<Long> lessonIds;
}
