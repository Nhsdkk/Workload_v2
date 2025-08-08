package com.main.workload.services;

import com.main.workload.dtos.CreateWorkloadDTO;
import com.main.workload.dtos.UpdateWorkloadDTO;
import com.main.workload.dtos.WorkloadExportDTO;
import com.main.workload.entities.*;
import com.main.workload.exceptions.InvalidValueException;
import com.main.workload.exceptions.ResourceNotFoundException;
import com.main.workload.exceptions.ServerException;
import com.main.workload.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

record CustomKey(String lessonName, String lessonSemester, Set<String> types, String teacher) {}


@Service
public class WorkloadService {
    private final AcademicLoadRepository academicLoadRepository;
    private final WorkloadRepository workloadRepository;
    private final WorkloadContainerRepository workloadContainerRepository;
    private final LessonRepository lessonRepository;
    private final StudentsGroupRepository studentsGroupRepository;
    private final EmployeePositionRepository employeePositionRepository;

    public WorkloadService(
            WorkloadRepository workloadRepository,
            LessonRepository lessonRepository,
            StudentsGroupRepository studentsGroupRepository,
            AcademicLoadRepository academicLoadRepository,
            WorkloadContainerRepository workloadContainerRepository,
            EmployeePositionRepository employeePositionRepository
    ) {
        this.workloadRepository = workloadRepository;
        this.lessonRepository = lessonRepository;
        this.studentsGroupRepository = studentsGroupRepository;
        this.academicLoadRepository = academicLoadRepository;
        this.workloadContainerRepository = workloadContainerRepository;
        this.employeePositionRepository = employeePositionRepository;
    }

    private Boolean sameWorkloadExists(Workload.WorkloadType workloadType, Lesson lesson, StudentsGroup group) {
        var potentialWorkloads = workloadRepository.findAllByGroupEqualsAndTypeEquals(group, workloadType);

        return potentialWorkloads
                .stream()
                .map(Workload::getContainer)
                .anyMatch(it -> Objects.equals(it.getLesson().getId(), lesson.getId()));
    }

    public static List<WorkloadExportDTO> aggregateContainers(List<WorkloadContainer> workloadContainers) {
        return workloadContainers.stream().map(WorkloadExportDTO::new).collect(Collectors.toMap(
                w -> new CustomKey(
                        w.getLessonName(),
                        w.getLessonSemester(),
                        Set.copyOf(w.getWorkloadTypes()),
                        w.getTeacherName()
                ),
                w -> {
                    var d = new WorkloadExportDTO();
                    d.setLessonName(w.getLessonName());
                    d.setTeacherName(w.getTeacherName());
                    d.setWorkloadTypes(new ArrayList<>(w.getWorkloadTypes()));
                    d.setId(new ArrayList<>(w.getId()));
                    d.setGroups(new ArrayList<>(w.getGroups()));
                    d.setWorkloadHours(w.getWorkloadHours());
                    return d;
                },
                (a, b) -> { // merge
                    a.getId().addAll(b.getId());
                    a.getGroups().addAll(b.getGroups());
                    a.setWorkloadHours(
                            (a.getWorkloadHours() == null ? 0 : a.getWorkloadHours()) +
                                    (b.getWorkloadHours() == null ? 0 : b.getWorkloadHours())
                    );
                    return a;
                }
        )).values().stream().toList();
    }

    public List<WorkloadExportDTO> createWorkload(CreateWorkloadDTO createWorkloadDTO) {
        var lesson = lessonRepository.findById(createWorkloadDTO.getLessonId());
        if (lesson.isEmpty()) {
            throw new ResourceNotFoundException("Lesson not found");
        }

        List<WorkloadContainer> containers = new ArrayList<>();
        if (createWorkloadDTO.getWorkloadType().contains(Workload.WorkloadType.LECTURE.toString())) {
            var container = new WorkloadContainer(lesson.get());
            createWorkloadDTO.getWorkloadType().forEach(_ -> containers.add(container));
        } else {
            createWorkloadDTO.getWorkloadType().forEach(_ -> containers.add(new WorkloadContainer(lesson.get())));
        }

        for (var i = 0; i < containers.size(); i++) {
            for (var groupId : createWorkloadDTO.getStudentGroupId()) {
                var group = studentsGroupRepository.findById(groupId);
                if (group.isEmpty()) {
                    throw new ResourceNotFoundException("Student group not found");
                }

                var associatedLoad = academicLoadRepository.findByGroupNameAndSubjectAndCourseAndSemester(
                        group.get().getName(),
                        lesson.get().getName(),
                        lesson.get().getCourse(),
                        lesson.get().getSemester()
                );

                if (associatedLoad.isEmpty()) {
                    throw new ServerException("Can't find load associated to existing student group and lesson");
                }

                if (sameWorkloadExists(Workload.WorkloadType.valueOf(createWorkloadDTO.getWorkloadType().get(i)), lesson.get(), group.get())) {
                    throw new InvalidValueException("Workload already exists");
                }

                if (createWorkloadDTO.getPositionId() != null) {
                    var employeePosition = employeePositionRepository.findById(createWorkloadDTO.getPositionId());
                    if (employeePosition.isEmpty()) {
                        throw new ResourceNotFoundException("Can't find teacher");
                    }
                    containers.get(i).setTeacher(employeePosition.get());
                }

                var workload = new Workload(
                        createWorkloadDTO.getActive(),
                        Workload.WorkloadType.valueOf(createWorkloadDTO.getWorkloadType().get(i)),
                        containers.get(i),
                        group.get(),
                        associatedLoad.get()
                );

                workloadRepository.save(workload);
            }
        }

        return containers.stream().distinct().map(WorkloadExportDTO::new).collect(Collectors.toList());
    }

    public void deleteWorkload(Long workloadId) {
        var workload = workloadRepository.findById(workloadId);
        if (workload.isEmpty()) {
            throw new ResourceNotFoundException("Workload not found");
        }

        workloadRepository.delete(workload.get());
    }

    public WorkloadExportDTO updateWorkload(UpdateWorkloadDTO updateWorkloadDTO) {
        var contIds = updateWorkloadDTO.getContainerIds();
        List<WorkloadContainer> conts = new ArrayList<>();

        var containerFirst = workloadContainerRepository.findById(contIds.getFirst());
        if (containerFirst.isEmpty()) {
            throw new ResourceNotFoundException("Container not found");
        }
        var workloadTypes = containerFirst.get().getWorkloads().stream().map(Workload::getType).toList();

        if (updateWorkloadDTO.getStudentGroupId() != null && !updateWorkloadDTO.getStudentGroupId().isEmpty()) {
            List<Long> newGroups = new ArrayList<>();
            for (var contId : contIds) {
                var container = workloadContainerRepository.findById(contId);
                if (container.isEmpty()) {
                    throw new ResourceNotFoundException("Container not found");
                }
                var workloads = container.get().getWorkloads();
                for (var workload : workloads) {
                    var groupId = workload.getGroup().getId();
                    if (!updateWorkloadDTO.getStudentGroupId().contains(groupId)) {
                        workloadRepository.delete(workload);
                    } else {
                        newGroups.add(groupId);
                    }
                }
                // Подтягиваем изменения
                container = workloadContainerRepository.findById(contId);
                if (container.isPresent() && container.get().getWorkloads().isEmpty() && contIds.size() != 1) {
                    contIds.remove(contId);
                    workloadContainerRepository.delete(container.get());
                } else container.ifPresent(conts::add);
            }
            var finalNewGroups = updateWorkloadDTO.getStudentGroupId().stream().filter(group -> !newGroups.contains(group)).toList();

            // Создание ворклоадов на каждый тип и группу O(n^2)
            for (var workloadType : workloadTypes) {
                for (var groupId : finalNewGroups) {
                    var studentGroup = studentsGroupRepository.findById(groupId);
                    if (studentGroup.isEmpty()) {
                        throw new ResourceNotFoundException("Student group not found");
                    }

                    var hoursAmount = academicLoadRepository.findByGroupNameAndSubjectAndCourseAndSemester(
                            studentGroup.get().getName(),
                            containerFirst.get().getLesson().getName(),
                            containerFirst.get().getLesson().getCourse(),
                            containerFirst.get().getLesson().getSemester()
                    );
                    if (hoursAmount.isEmpty()) {
                        throw new ResourceNotFoundException("Workload not found");
                    }
                    var newWorkload = new Workload(
                            containerFirst.get().getWorkloads().getFirst().getActive(),
                            workloadType,
                            containerFirst.get(),
                            studentGroup.get(),
                            hoursAmount.get()
                    );
                    workloadRepository.save(newWorkload);
                }
            }
        }

        var isNullLesson = updateWorkloadDTO.getLessonId() == null;
        var isNullPosition = updateWorkloadDTO.getEmployeePositionId() == null;
        for (var containerId : contIds) {
            var container = workloadContainerRepository.findById(containerId);
            if (container.isEmpty()) {
                throw new ResourceNotFoundException("Container not found");
            }
            if (!isNullLesson) {
                var lesson = lessonRepository.findById(updateWorkloadDTO.getLessonId());
                if (lesson.isEmpty()) {
                    throw new ResourceNotFoundException("Lesson not found");
                }

                container.get().setLesson(lesson.get());
            }

            if (!isNullPosition) {
                var employeePosition = employeePositionRepository.findById(updateWorkloadDTO.getEmployeePositionId());
                if (employeePosition.isEmpty()) {
                    throw new ResourceNotFoundException("Employee position not found");
                }

                container.get().setPosition(employeePosition.get());
            }
            workloadContainerRepository.save(container.get());
        }

        return aggregateContainers(conts).getFirst();
    }
}
