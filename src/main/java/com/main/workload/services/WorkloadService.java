package com.main.workload.services;

import com.main.workload.dtos.CreateWorkloadDTO;
import com.main.workload.dtos.UpdateWorkloadDTO;
import com.main.workload.dtos.WorkloadExportDTO;
import com.main.workload.entities.Lesson;
import com.main.workload.entities.StudentsGroup;
import com.main.workload.entities.Workload;
import com.main.workload.entities.WorkloadContainer;
import com.main.workload.exceptions.InvalidValueException;
import com.main.workload.exceptions.ResourceNotFoundException;
import com.main.workload.exceptions.ServerException;
import com.main.workload.repositories.*;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public WorkloadExportDTO createWorkload(CreateWorkloadDTO createWorkloadDTO) {
        var lesson = lessonRepository.findById(createWorkloadDTO.getLessonId());
        if (lesson.isEmpty()) {
            throw new ResourceNotFoundException("Lesson not found");
        }

        var group = studentsGroupRepository.findById(createWorkloadDTO.getStudentGroupId());
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

        if (sameWorkloadExists(Workload.WorkloadType.valueOf(createWorkloadDTO.getWorkloadType()), lesson.get(), group.get())) {
            throw new InvalidValueException("Workload already exists");
        }

        var workloadContainer = new WorkloadContainer(lesson.get());

        if (createWorkloadDTO.getPositionId() != null) {
            var employeePosition = employeePositionRepository.findById(createWorkloadDTO.getPositionId());
            if (employeePosition.isEmpty()) {
                throw new ResourceNotFoundException("Can't find teacher");
            }
            workloadContainer.setTeacher(employeePosition.get());
        }

        var workload = new Workload(
                createWorkloadDTO,
                workloadContainer,
                group.get(),
                associatedLoad.get()
        );

        workloadRepository.save(workload);

        return new WorkloadExportDTO(workloadContainer);
    }

    public void deleteWorkload(Long workloadId) {
        var workload = workloadRepository.findById(workloadId);
        if (workload.isEmpty()) {
            throw new ResourceNotFoundException("Workload not found");
        }

        workloadRepository.delete(workload.get());
    }

    public WorkloadExportDTO updateWorkload(UpdateWorkloadDTO updateWorkloadDTO) {
        var workload = workloadRepository.findById(updateWorkloadDTO.getId());
        if (workload.isEmpty()) {
            throw new ResourceNotFoundException("Workload not found");
        }

        if (updateWorkloadDTO.getLessonId() != null) {
            var lesson = lessonRepository.findById(updateWorkloadDTO.getLessonId());
            if (lesson.isEmpty()) {
                throw new ResourceNotFoundException("Lesson not found");
            }

            workload.get().getContainer().setLesson(lesson.get());
        }

        if (updateWorkloadDTO.getStudentGroupId() != null) {
            var studentGroup = studentsGroupRepository.findById(updateWorkloadDTO.getStudentGroupId());
            if (studentGroup.isEmpty()) {
                throw new ResourceNotFoundException("Student group not found");
            }

            workload.get().setGroup(studentGroup.get());
        }

        if (updateWorkloadDTO.getEmployeePositionId() != null) {
            var employeePosition = employeePositionRepository.findById(updateWorkloadDTO.getEmployeePositionId());
            if (employeePosition.isEmpty()) {
                throw new ResourceNotFoundException("Employee position not found");
            }

            workload.get().getContainer().setPosition(employeePosition.get());
        }

        workload.get().setType(
                updateWorkloadDTO.getWorkloadType() == null ?
                        workload.get().getType() :
                        Workload.WorkloadType.valueOf(updateWorkloadDTO.getWorkloadType())
        );

        if (sameWorkloadExists(workload.get().getType(), workload.get().getContainer().getLesson(), workload.get().getGroup())) {
            throw new InvalidValueException("Workload already exists");
        }

        workloadRepository.save(workload.get());

        return new WorkloadExportDTO(workload.get().getContainer());
    }
}
