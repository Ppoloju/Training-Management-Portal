package com.training.portal.repository;

import com.training.portal.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends CrudRepository<Course, UUID>, PagingAndSortingRepository<Course, UUID> {
    
    Optional<Course> findByIdAndDeletedFalse(UUID id);
    
    Page<Course> findAllByDeletedFalse(Pageable pageable);
    
    boolean existsByTitleAndDeletedFalse(String title);
    
    boolean existsByTitleAndIdNotAndDeletedFalse(String title, UUID id);
}
