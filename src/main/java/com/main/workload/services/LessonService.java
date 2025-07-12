package com.main.workload.services;

import com.main.workload.dtos.CreateLessonDTO;
import com.main.workload.dtos.LessonDTO;
import com.main.workload.dtos.UpdateLessonDTO;
import com.main.workload.entities.Lesson;
import com.main.workload.exceptions.ResourceNotFoundException;
import com.main.workload.repositories.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<LessonDTO> getAllLessons() {
        return lessonRepository
                .findAll()
                .stream()
                .map(LessonDTO::new)
                .collect(Collectors.toList());
    }

    public LessonDTO addLesson(CreateLessonDTO lessonDto) {
        var lesson = new Lesson(lessonDto);
        lessonRepository.save(lesson);
        return new LessonDTO(lesson);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    public LessonDTO updateLesson(UpdateLessonDTO updateLessonDTO) {
        var lesson = lessonRepository.findById(updateLessonDTO.getId());
        if (lesson.isEmpty()) {
            throw new ResourceNotFoundException("Can't find lesson to update");
        }
        lesson.get().Update(updateLessonDTO);
        lessonRepository.save(lesson.get());
        return new LessonDTO(lesson.get());
    }
}
