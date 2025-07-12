package com.main.workload.controllers;

import com.main.workload.dtos.CreateLessonDTO;
import com.main.workload.dtos.LessonDTO;
import com.main.workload.dtos.UpdateLessonDTO;
import com.main.workload.services.LessonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@Tag(name = "Предметы", description = "API для управления предметами")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping
    public List<LessonDTO> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @PostMapping
    public LessonDTO createLesson(@RequestBody CreateLessonDTO lessonDTO) {
        return lessonService.addLesson(lessonDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
    }

    @PutMapping
    public LessonDTO updateLesson(@RequestBody UpdateLessonDTO updateLessonDTO) {
        return lessonService.updateLesson(updateLessonDTO);
    }
}
