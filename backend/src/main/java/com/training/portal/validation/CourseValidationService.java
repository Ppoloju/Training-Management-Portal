package com.training.portal.validation;

import com.training.portal.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseValidationService {

    private final CourseRepository courseRepository;

    public void validateTitleUnique(String title) {
        if (courseRepository.existsByTitleAndDeletedFalse(title)) {
            throw new IllegalArgumentException("Course with title '" + title + "' already exists");
        }
    }

    public void validateTitleUniqueForUpdate(String title, UUID id) {
        if (courseRepository.existsByTitleAndIdNotAndDeletedFalse(title, id)) {
            throw new IllegalArgumentException("Course with title '" + title + "' already exists");
        }
    }
}
