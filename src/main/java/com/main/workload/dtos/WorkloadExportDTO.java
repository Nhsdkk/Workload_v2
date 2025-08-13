package com.main.workload.dtos;

import com.main.workload.entities.StudentsGroup;
import com.main.workload.entities.Workload;
import com.main.workload.entities.WorkloadContainer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class WorkloadExportDTO {
    private List<Long> id;
    private String lessonName;
    private Long lessonId;
    private String lessonSemester;
    private List<String> workloadTypes;
    private Integer workloadHours;
    private String teacherName;
    private List<GroupDTO> groups;

    public WorkloadExportDTO(WorkloadContainer wc) {
        // Наименование дисциплины
        Long lessonId = wc.getLesson() != null ? wc.getLesson().getId() : -1;
        String lessonName = wc.getLesson() != null ? wc.getLesson().getName() : "N/A";
        String lessonSemester = wc.getLesson() != null ? wc.getLesson().getSemester().toString() : "N/A";

        // Уникальные типы нагрузки
        Set<String> workloadTypes = wc.getWorkloads().stream()
                .map(w -> w.getType().getTranslation())
                .collect(Collectors.toSet());

        // Часы нагрузки
        Integer hours = wc.getWorkloadHours();

        // Преподаватель
        String teacher = wc.getPosition() == null ? "Не назначено" : wc.getPosition().getEmployee().getName();

        // Список групп
        List<GroupDTO> groups = wc.getWorkloads().stream()
                .map(Workload::getGroup)
                .filter(Objects::nonNull)
                .map(GroupDTO::new)
                .distinct()
                .toList();

        setLessonName(lessonName);
        setWorkloadHours(hours);
        setTeacherName(teacher);
        setGroups(groups);
        setLessonId(lessonId);
        setLessonSemester(lessonSemester);
        setWorkloadTypes(workloadTypes.stream().toList());
        setId(List.of(wc.getId()));
    }

    public WorkloadExportDTO() {
        this.id = new ArrayList<>();
        this.workloadTypes = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    @Data
    public static class GroupDTO {
        private Long id;
        private String name;

        public GroupDTO(StudentsGroup group) {
            setId(group.getId());
            setName(group.getName());
        }
    }
}
