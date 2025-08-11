package com.main.workload.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Workload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkloadType type;

    @Column(nullable = false)
    private int workload;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "container_id")
    private WorkloadContainer container;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private StudentsGroup group;

    private Boolean active;

    public Workload(WorkloadType type, int workload, StudentsGroup group) {
        this.type = type;
        this.workload = workload;
        this.group = group;
        this.active = true;
    }

    public Workload(
            boolean isActive,
            WorkloadType workloadType,
            WorkloadContainer container,
            StudentsGroup group,
            AcademicLoad load
    ) {
        setActive(isActive);
        setType(workloadType);
        setGroup(group);
        setWorkload(load.getEstimatedWorkload(workloadType));
        container.addWorkload(this);
    }

    @Getter
    public enum WorkloadType {
        LECTURE("Лекция"),
        PRACTICE("Практика"),
        LABORATORY_WORK("Лабораторная работа"),
        EXAM("Экзамен"),
        RATING("Рейтинг"),
        CREDIT("Зачет"),
        CONSULT("Консультация"),
        KSR("КСР"),
        DIPLOMA("Диплом"),
        OTHER("Другое"),
        COURSE_WORK("Курсовая работа"),
        COURSE_PROJECT("Курсовой проект");

        private final String translation;

        public static @NonNull WorkloadType fromDisplayName(@NonNull String displayName) throws IllegalArgumentException {
            for (WorkloadType workload : WorkloadType.values()) {
                if (workload.getTranslation().equalsIgnoreCase(displayName)) {
                    return workload;
                }
            }
            throw new IllegalArgumentException("No workload type found for display name: " + displayName);
        }

        WorkloadType(String translation) {
            this.translation = translation;
        }
    }

    @Override
    public String toString() {
        return "Workload{" +
                "id=" + id +
                ", type=" + type +
                ", workload=" + workload +
                '}';
    }
}
