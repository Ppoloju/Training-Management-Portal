package com.training.portal.service.impl;

import com.training.portal.exception.ResourceNotFoundException;
import com.training.portal.model.Course;
import com.training.portal.repository.CourseRepository;
import com.training.portal.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public Course save(Course course) {
        return Objects.requireNonNull(courseRepository.save(course));
    }

    @Override
    @Transactional(readOnly = true)
    public Course findById(UUID id) {
        Course course = courseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setNewEntity(false);
        return course;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    @Transactional
    public void delete(Course course) {
        course.setDeleted(true);
        courseRepository.save(course);
    }
}
