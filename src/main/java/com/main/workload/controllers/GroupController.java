package com.main.workload.controllers;

import com.main.workload.dtos.GroupByLessonDTO;
import com.main.workload.services.StudentGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groups")
@Tag(name = "Группы", description = "API для работы с группами")
public class GroupController {
    private final StudentGroupService studentGroupService;

    public GroupController(
            StudentGroupService studentGroupService
    ) {
        this.studentGroupService = studentGroupService;
    }

    @GetMapping
    public List<GroupByLessonDTO> getAllWithLessonId(@RequestParam Long lessonId) throws Exception {
        return studentGroupService.getAllStudentGroupsByLesson(lessonId);
    }
}
