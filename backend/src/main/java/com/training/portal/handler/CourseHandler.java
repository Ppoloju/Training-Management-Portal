package com.training.portal.handler;

import com.training.portal.dto.request.CourseCreateRequest;
import com.training.portal.dto.request.CourseUpdateRequest;
import com.training.portal.dto.response.CourseResponse;
import com.training.portal.dto.response.PageResponse;
import com.training.portal.mapper.CourseMapper;
import com.training.portal.model.Course;
import com.training.portal.service.CourseService;
import com.training.portal.validation.CourseValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CourseHandler {

    private final CourseService courseService;
    private final CourseValidationService validationService;
    private final CourseMapper courseMapper;

    public CourseResponse createCourse(CourseCreateRequest request) {
        validationService.validateTitleUnique(request.title());

        Course course = courseMapper.toEntity(request);
        if (course.getId() == null) {
            course.setId(java.util.UUID.randomUUID());
        }
        course.setNewEntity(true);
        course.setCreatedAt(java.time.LocalDateTime.now());
        course.setUpdatedAt(java.time.LocalDateTime.now());
        Course savedCourse = courseService.save(course);

        return courseMapper.toResponse(savedCourse);
    }

    public CourseResponse getCourse(UUID id) {
        Course course = courseService.findById(id);
        return courseMapper.toResponse(course);
    }

    public PageResponse<CourseResponse> getAllCourses(int page, int size) {
        Page<Course> coursePage = courseService.findAll(PageRequest.of(page, size));
        
        List<CourseResponse> content = coursePage.getContent()
                .stream()
                .map(courseMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                coursePage.getNumber(),
                coursePage.getSize(),
                coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                coursePage.isLast()
        );
    }

    public CourseResponse updateCourse(UUID id, CourseUpdateRequest request) {
        validationService.validateTitleUniqueForUpdate(request.title(), id);

        Course existingCourse = courseService.findById(id);
        courseMapper.updateEntityFromRequest(request, existingCourse);
        existingCourse.setUpdatedAt(java.time.LocalDateTime.now());

        Course updatedCourse = courseService.save(existingCourse);

        return courseMapper.toResponse(updatedCourse);
    }

    public void deleteCourse(UUID id) {
        Course course = courseService.findById(id);
        courseService.delete(course);
    }
}
