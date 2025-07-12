package com.main.workload.entities;

import com.main.workload.dtos.CreateLessonDTO;
import com.main.workload.dtos.UpdateLessonDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lesson")
@Data
@NoArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String name;

    private Integer semester;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkloadContainer> containers;

    @ManyToMany(mappedBy = "availableLessons")
    private List<Employee> qualifiedEmployees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        qualifiedEmployees.add(employee);
    }

    public Lesson(String name, Integer semester) {
        this.name = name;
        this.semester = semester;
    }

    public Lesson(CreateLessonDTO lessonDto) {
        this(lessonDto.getName(), lessonDto.getSemester());
    }

    public void Update(UpdateLessonDTO updateLessonDTO) {
        semester = (updateLessonDTO.getSemester() != null) ? updateLessonDTO.getSemester() : semester;
        name = (updateLessonDTO.getName() != null) ? updateLessonDTO.getName() : name;
    }

    @PreRemove
    private void removeLinks() {
        for (var employee : qualifiedEmployees) {
            employee.getAvailableLessons().remove(this);
        }
    }
}
