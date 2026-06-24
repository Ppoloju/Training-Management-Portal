package com.training.portal.config;

import com.training.portal.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;

import java.util.UUID;

@Configuration
public class DatabaseConfig {

    @Bean
    public BeforeConvertCallback<Employee> employeeIdGenerator() {
        return (employee) -> {
            if (employee.getId() == null)
                employee.setId(UUID.randomUUID());
            return employee;
        };
    }

    @Bean
    public BeforeConvertCallback<Course> courseIdGenerator() {
        return (course) -> {
            if (course.getId() == null)
                course.setId(UUID.randomUUID());
            return course;
        };
    }

    @Bean
    public BeforeConvertCallback<Trainer> trainerIdGenerator() {
        return (trainer) -> {
            if (trainer.getId() == null)
                trainer.setId(UUID.randomUUID());
            return trainer;
        };
    }

    @Bean
    public BeforeConvertCallback<TrainingProgram> trainingProgramIdGenerator() {
        return (program) -> {
            if (program.getId() == null)
                program.setId(UUID.randomUUID());
            return program;
        };
    }

    @Bean
    public BeforeConvertCallback<TrainingSession> trainingSessionIdGenerator() {
        return (session) -> {
            if (session.getId() == null)
                session.setId(UUID.randomUUID());
            return session;
        };
    }

    @Bean
    public BeforeConvertCallback<Enrollment> enrollmentIdGenerator() {
        return (enrollment) -> {
            if (enrollment.getId() == null)
                enrollment.setId(UUID.randomUUID());
            return enrollment;
        };
    }

    @Bean
    public BeforeConvertCallback<Attendance> attendanceIdGenerator() {
        return (attendance) -> {
            if (attendance.getId() == null)
                attendance.setId(UUID.randomUUID());
            return attendance;
        };
    }
}
