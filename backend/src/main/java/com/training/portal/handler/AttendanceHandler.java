package com.training.portal.handler;

import com.training.portal.dto.request.AttendanceCreateRequest;
import com.training.portal.dto.response.AttendanceResponse;
import com.training.portal.mapper.AttendanceMapper;
import com.training.portal.model.Attendance;
import com.training.portal.service.AttendanceService;
import com.training.portal.validation.AttendanceValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceHandler {

    private final AttendanceService attendanceService;
    private final AttendanceValidationService validationService;
    private final AttendanceMapper attendanceMapper;

    public AttendanceResponse createAttendance(AttendanceCreateRequest request) {
        validationService.validateAttendanceRecord();

        Attendance attendance = attendanceMapper.toEntity(request);
        if (attendance.getId() == null) {
            attendance.setId(java.util.UUID.randomUUID());
        }
        attendance.setNewEntity(true);
        attendance.setCreatedAt(java.time.LocalDateTime.now());
        attendance.setUpdatedAt(java.time.LocalDateTime.now());
        Attendance savedAttendance = attendanceService.save(attendance);

        return attendanceMapper.toResponse(savedAttendance);
    }

    public AttendanceResponse getAttendance(java.util.UUID id) {
        Attendance attendance = attendanceService.findById(id);
        return attendanceMapper.toResponse(attendance);
    }

    public void deleteAttendance(java.util.UUID id) {
        Attendance attendance = attendanceService.findById(id);
        attendanceService.delete(attendance);
    }

    public com.training.portal.dto.response.PageResponse<AttendanceResponse> getAllAttendances(int page, int size) {
        org.springframework.data.domain.Page<Attendance> attendancePage = attendanceService.findAll(org.springframework.data.domain.PageRequest.of(page, size));
        
        java.util.List<AttendanceResponse> content = attendancePage.getContent()
                .stream()
                .map(attendanceMapper::toResponse)
                .toList();

        return new com.training.portal.dto.response.PageResponse<>(
                content,
                attendancePage.getNumber(),
                attendancePage.getSize(),
                attendancePage.getTotalElements(),
                attendancePage.getTotalPages(),
                attendancePage.isLast()
        );
    }

    public AttendanceResponse updateAttendance(java.util.UUID id, com.training.portal.dto.request.AttendanceUpdateRequest request) {
        Attendance existingAttendance = attendanceService.findById(id);
        attendanceMapper.updateEntityFromRequest(request, existingAttendance);
        existingAttendance.setUpdatedAt(java.time.LocalDateTime.now());

        Attendance updatedAttendance = attendanceService.save(existingAttendance);

        return attendanceMapper.toResponse(updatedAttendance);
    }
}
