package com.training.portal.service;

import com.training.portal.model.Attendance;

import java.util.UUID;

public interface AttendanceService {

    Attendance save(Attendance attendance);

    Attendance findById(UUID id);

    void delete(Attendance attendance);

    org.springframework.data.domain.Page<Attendance> findAll(org.springframework.data.domain.Pageable pageable);
}
