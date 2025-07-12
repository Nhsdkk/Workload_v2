package com.main.workload.dtos;

import com.main.workload.entities.Lesson;
import lombok.Data;

@Data
public class LessonDTO {
    private Long id;
    private String name;
    private Integer semester;

    public LessonDTO(Lesson lesson) {
        id = lesson.getId();
        name = lesson.getName();
        semester = lesson.getSemester();
    }
}

