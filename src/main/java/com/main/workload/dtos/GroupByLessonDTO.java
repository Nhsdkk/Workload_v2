package com.main.workload.dtos;

import com.main.workload.entities.StudentsGroup;
import lombok.Data;

@Data
public class GroupByLessonDTO {
    private Long id;
    private String name;
    private Integer studentCount;

    public GroupByLessonDTO(StudentsGroup group) {
        setId(group.getId());
        setName(group.getName());
        setStudentCount(group.getStudentsCount());
    }
}
