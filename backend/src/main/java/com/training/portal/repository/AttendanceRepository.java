package com.training.portal.repository;

import com.training.portal.model.Attendance;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AttendanceRepository extends CrudRepository<Attendance, UUID>, PagingAndSortingRepository<Attendance, UUID> {

    Optional<Attendance> findByIdAndDeletedFalse(UUID id);
    
    Page<Attendance> findAllByDeletedFalse(Pageable pageable);
}
