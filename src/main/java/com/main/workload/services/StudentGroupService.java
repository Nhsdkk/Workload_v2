package com.main.workload.services;

import com.main.workload.dtos.GroupByLessonDTO;
import com.main.workload.entities.AcademicLoad;
import com.main.workload.exceptions.ResourceNotFoundException;
import com.main.workload.repositories.AcademicLoadRepository;
import com.main.workload.repositories.LessonRepository;
import com.main.workload.repositories.StudentsGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentGroupService {

    private final LessonRepository lessonRepository;
    private final StudentsGroupRepository studentsGroupRepository;
    private final AcademicLoadRepository academicLoadRepository;

    public StudentGroupService(
            LessonRepository lessonRepository,
            StudentsGroupRepository studentsGroupRepository,
            AcademicLoadRepository academicLoadRepository
    ) {
        this.lessonRepository = lessonRepository;
        this.studentsGroupRepository = studentsGroupRepository;
        this.academicLoadRepository = academicLoadRepository;
    }

    public List<GroupByLessonDTO> getAllStudentGroupsByLesson(Long lessonId) throws Exception {
        var lesson = lessonRepository.findById(lessonId);
        if (lesson.isEmpty()) {
            throw new ResourceNotFoundException("Lesson Not Found");
        }

        var academicLoads = academicLoadRepository.findBySubjectAndCourse(lesson.get().getName(), lesson.get().getCourse());
        var groupsNames = academicLoads
                .stream()
                .map(AcademicLoad::getGroupName)
                .toList();

        var groups = studentsGroupRepository.findAllByNameIn(groupsNames);
        if (groups.size() != groupsNames.size()) {
            throw new Exception("Could not get all groups");
        }

        return groups
                .stream()
                .map(GroupByLessonDTO::new)
                .toList();
    }
}
