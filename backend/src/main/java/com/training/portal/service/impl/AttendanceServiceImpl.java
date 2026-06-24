package com.training.portal.service.impl;

import com.training.portal.exception.ResourceNotFoundException;
import com.training.portal.model.Attendance;
import com.training.portal.repository.AttendanceRepository;
import com.training.portal.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public Attendance save(Attendance attendance) {
        return Objects.requireNonNull(attendanceRepository.save(attendance));
    }

    @Override
    @Transactional(readOnly = true)
    public Attendance findById(UUID id) {
        Attendance attendance = attendanceRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: " + id));
        attendance.setNewEntity(false);
        return attendance;
    }

    @Override
    @Transactional
    public void delete(Attendance attendance) {
        attendance.setDeleted(true);
        attendanceRepository.save(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Attendance> findAll(org.springframework.data.domain.Pageable pageable) {
        return attendanceRepository.findAllByDeletedFalse(pageable);
    }
}
