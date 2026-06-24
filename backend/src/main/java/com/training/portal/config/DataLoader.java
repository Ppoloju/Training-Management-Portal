package com.training.portal.config;

import com.training.portal.constants.CourseStatus;
import com.training.portal.constants.TrainingProgramStatus;
import com.training.portal.model.*;
import com.training.portal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final TrainerRepository trainerRepository;
    private final CourseRepository courseRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (employeeRepository.count() > 0) {
            log.info("Data already loaded, skipping data loader");
            return;
        }

        log.info("Loading demo data...");

        // Create Trainers
        Trainer trainer1 = createTrainer("John", "Smith", "john.smith@example.com", "Java Development");
        Trainer trainer2 = createTrainer("Sarah", "Johnson", "sarah.johnson@example.com", "Project Management");
        Trainer trainer3 = createTrainer("Michael", "Brown", "michael.brown@example.com", "Data Science");
        
        trainerRepository.save(trainer1);
        trainerRepository.save(trainer2);
        trainerRepository.save(trainer3);

        // Create Courses
        Course course1 = createCourse("Java Fundamentals", "Introduction to Java programming", 
            LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), CourseStatus.PUBLISHED);
        Course course2 = createCourse("Advanced Java", "Advanced Java concepts and patterns", 
            LocalDate.now().plusDays(10), LocalDate.now().plusDays(15), CourseStatus.PUBLISHED);
        Course course3 = createCourse("Project Management Basics", "Introduction to PM methodologies", 
            LocalDate.now().plusDays(5), LocalDate.now().plusDays(8), CourseStatus.PUBLISHED);
        
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        // Create Training Programs
        TrainingProgram program1 = createTrainingProgram("Java Bootcamp", "Intensive Java training program",
            LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), TrainingProgramStatus.IN_PROGRESS, 
            20, trainer1.getId(), course1.getId());
        TrainingProgram program2 = createTrainingProgram("PM Certification Prep", "Project management certification preparation",
            LocalDate.now().plusDays(5), LocalDate.now().plusDays(8), TrainingProgramStatus.PLANNED, 
            15, trainer2.getId(), course3.getId());
        TrainingProgram program3 = createTrainingProgram("Data Science Workshop", "Hands-on data science training",
            LocalDate.now().plusDays(10), LocalDate.now().plusDays(15), TrainingProgramStatus.PLANNED, 
            10, trainer3.getId(), course2.getId());
        
        trainingProgramRepository.save(program1);
        trainingProgramRepository.save(program2);
        trainingProgramRepository.save(program3);

        // Create Training Sessions
        TrainingSession session1 = createTrainingSession("Java Session 1", "First Java session",
            LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), program1.getId(), "Room A", 20);
        TrainingSession session2 = createTrainingSession("PM Session 1", "First PM session",
            LocalDate.now().plusDays(5), LocalDate.now().plusDays(6), program2.getId(), "Room B", 15);
        TrainingSession session3 = createTrainingSession("Data Science Session 1", "First DS session",
            LocalDate.now().plusDays(10), LocalDate.now().plusDays(11), program3.getId(), "Room C", 10);
        
        trainingSessionRepository.save(session1);
        trainingSessionRepository.save(session2);
        trainingSessionRepository.save(session3);

        // Create Employees
        Employee emp1 = createEmployee("Alice", "Williams", "alice.williams@example.com", "Engineering", true);
        Employee emp2 = createEmployee("Bob", "Davis", "bob.davis@example.com", "Engineering", true);
        Employee emp3 = createEmployee("Carol", "Miller", "carol.miller@example.com", "HR", true);
        Employee emp4 = createEmployee("David", "Wilson", "david.wilson@example.com", "Finance", true);
        Employee emp5 = createEmployee("Emma", "Taylor", "emma.taylor@example.com", "Marketing", true);
        Employee emp6 = createEmployee("Frank", "Anderson", "frank.anderson@example.com", "Engineering", false);
        Employee emp7 = createEmployee("Grace", "Thomas", "grace.thomas@example.com", "IT", true);
        Employee emp8 = createEmployee("Henry", "Jackson", "henry.jackson@example.com", "Operations", true);
        
        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        employeeRepository.save(emp3);
        employeeRepository.save(emp4);
        employeeRepository.save(emp5);
        employeeRepository.save(emp6);
        employeeRepository.save(emp7);
        employeeRepository.save(emp8);

        // Create Enrollments
        Enrollment enrollment1 = createEnrollment(emp1.getId(), session1.getId());
        Enrollment enrollment2 = createEnrollment(emp2.getId(), session1.getId());
        Enrollment enrollment3 = createEnrollment(emp3.getId(), session2.getId());
        Enrollment enrollment4 = createEnrollment(emp4.getId(), session2.getId());
        Enrollment enrollment5 = createEnrollment(emp5.getId(), session3.getId());
        
        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);
        enrollmentRepository.save(enrollment3);
        enrollmentRepository.save(enrollment4);
        enrollmentRepository.save(enrollment5);

        // Create Attendance records
        attendanceRepository.save(createAttendance(enrollment1.getId(), true));
        attendanceRepository.save(createAttendance(enrollment2.getId(), true));
        attendanceRepository.save(createAttendance(enrollment3.getId(), false));
        attendanceRepository.save(createAttendance(enrollment4.getId(), true));

        log.info("Demo data loaded successfully!");
    }

    private Trainer createTrainer(String firstName, String lastName, String email, String specialization) {
        return Trainer.builder()
            .id(UUID.randomUUID())
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .specialization(specialization)
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .newEntity(true)
            .deleted(false)
            .build();
    }

    private Course createCourse(String title, String description, LocalDate startDate, LocalDate endDate, CourseStatus status) {
        return Course.builder()
            .id(UUID.randomUUID())
            .title(title)
            .description(description)
            .startDate(startDate)
            .endDate(endDate)
            .status(status)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .newEntity(true)
            .deleted(false)
            .build();
    }

    private TrainingProgram createTrainingProgram(String title, String description, LocalDate startDate, 
            LocalDate endDate, TrainingProgramStatus status, Integer maxParticipants, UUID trainerId, UUID courseId) {
        return TrainingProgram.builder()
            .id(UUID.randomUUID())
            .title(title)
            .description(description)
            .startDate(startDate)
            .endDate(endDate)
            .status(status)
            .maxParticipants(maxParticipants)
            .trainerId(trainerId)
            .courseId(courseId)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .newEntity(true)
            .deleted(false)
            .build();
    }

    private TrainingSession createTrainingSession(String title, String description, LocalDate startDate, 
            LocalDate endDate, UUID trainingProgramId, String location, Integer capacity) {
        return TrainingSession.builder()
            .id(UUID.randomUUID())
            .title(title)
            .description(description)
            .startDate(startDate)
            .endDate(endDate)
            .trainingProgramId(trainingProgramId)
            .location(location)
            .capacity(capacity)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .newEntity(true)
            .deleted(false)
            .build();
    }

    private Employee createEmployee(String firstName, String lastName, String email, String department, boolean isActive) {
        return Employee.builder()
            .id(UUID.randomUUID())
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .department(department)
            .isActive(isActive)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .newEntity(true)
            .deleted(false)
            .build();
    }

    private Enrollment createEnrollment(UUID employeeId, UUID trainingSessionId) {
        return Enrollment.builder()
            .id(UUID.randomUUID())
            .employeeId(employeeId)
            .trainingSessionId(trainingSessionId)
            .enrolledAt(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .newEntity(true)
            .deleted(false)
            .build();
    }

    private Attendance createAttendance(UUID enrollmentId, boolean present) {
        return Attendance.builder()
            .id(UUID.randomUUID())
            .enrollmentId(enrollmentId)
            .attendedAt(LocalDateTime.now())
            .present(present)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .newEntity(true)
            .deleted(false)
            .build();
    }
}
