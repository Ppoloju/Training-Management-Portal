package com.training.portal.service;

import com.training.portal.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CourseService {
    
    Course save(Course course);
    
    Course findById(UUID id);
    
    Page<Course> findAll(Pageable pageable);
    
    void delete(Course course);
}
