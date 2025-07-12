package com.main.workload.entities;

import com.main.workload.dtos.CreateEmployeeDTO;
import com.main.workload.dtos.UpdateEmployeeDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    private String typeOfEmployment; // Вид занятости


    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeePosition> positions;

    @ManyToMany
    @JoinTable(name = "employee_lesson", joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    private List<Lesson> availableLessons = new ArrayList<>();

    public void addLesson(Lesson lesson) {
        availableLessons.add(lesson);
    }
    public void addLessons(List<Lesson> lesson) {
        availableLessons.addAll(lesson);
    }

    public Employee(String name, String typeOfEmployment) {
        this.name = name;
        this.typeOfEmployment = typeOfEmployment;
        this.positions = new ArrayList<>();
    }

    public Employee(CreateEmployeeDTO dto) {
        this(dto.getName(), dto.getTypeOfEmployment());
        var position = new EmployeePosition(dto.getPosition());
        addPosition(position);
    }

    public void addPosition(@NonNull EmployeePosition position) {
        position.setEmployee(this);
        positions.add(position);
    }

    @PreRemove
    private void removeLessonLinks(){
        for (var lesson : availableLessons){
            lesson.getQualifiedEmployees().remove(this);
        }
    }

    public void Update(UpdateEmployeeDTO updateEmployeeDTO) {
        setName(updateEmployeeDTO.getName() == null ? name : updateEmployeeDTO.getName());
        setTypeOfEmployment(updateEmployeeDTO.getTypeOfEmployment() == null ? typeOfEmployment : updateEmployeeDTO.getTypeOfEmployment());
    }
}

