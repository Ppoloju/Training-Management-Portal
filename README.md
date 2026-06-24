# Training Management Portal

A full-stack web application for managing corporate training programs, employees, trainers, and course enrollments.

## Purpose

The Training Management Portal helps organizations streamline their training workflows by providing a centralized platform to:
- Manage employee training records and professional development
- Coordinate trainers and their specializations
- Create and track training programs and courses
- Handle employee enrollments in training sessions
- Record and monitor attendance

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.5.15 (Java 21)
- **Database**: PostgreSQL 16
- **ORM**: Spring Data JDBC
- **Security**: Spring Security with JWT Authentication
- **Database Migration**: Liquibase
- **Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Build Tool**: Gradle 8.14.5

### Frontend
- **Framework**: React 18 with TypeScript
- **Build Tool**: Vite 8
- **UI Library**: Material-UI (MUI) 5
- **State Management**: TanStack Query (React Query)
- **Routing**: React Router DOM 6
- **Form Handling**: React Hook Form with Yup validation
- **HTTP Client**: Axios

### Infrastructure
- **Containerization**: Docker, Docker Compose
- **Database**: PostgreSQL 16 (Docker container)

## Project Structure

```
training-management-portal/
├── backend/                          # Spring Boot backend application
│   ├── src/main/java/com/training/portal/
│   │   ├── config/                   # Configuration classes
│   │   │   └── security/             # Security and JWT configuration
│   │   ├── constants/                # Enum constants (status types)
│   │   ├── controller/               # REST API endpoints
│   │   ├── dto/                      # Data Transfer Objects
│   │   │   ├── request/              # Request DTOs
│   │   │   └── response/             # Response DTOs
│   │   ├── exception/                # Global exception handling
│   │   ├── handler/                  # Business logic handlers
│   │   ├── mapper/                   # MapStruct mappers
│   │   ├── model/                    # Entity models
│   │   ├── repository/               # Data access layer
│   │   ├── service/                  # Service interfaces
│   │   │   └── impl/                 # Service implementations
│   │   └── validation/               # Validation services
│   └── src/main/resources/
│       └── db/changelog/             # Liquibase migration scripts
│
├── frontend/                         # React frontend application
│   └── src/
│       ├── components/               # Reusable UI components
│       ├── context/                  # React context providers
│       ├── layouts/                  # Page layout components
│       ├── pages/                    # Page components
│       ├── routes/                   # Route configuration
│       ├── services/                 # API service modules
│       ├── types/                    # TypeScript type definitions
│       └── utils/                    # Utility functions
│
├── docker-compose.yml                # Docker Compose configuration
├── .env.example                      # Environment variables template
└── README.md                         # This file
```

## Core Features

### Employee Management
- Create, update, and manage employee records
- Track employee departments and active status
- View employee training history

### Trainer Management
- Manage trainer profiles with specializations
- Associate trainers with training programs
- Track trainer availability

### Course Management
- Create and configure courses
- Define course schedules and status
- Link courses to training programs

### Training Program Management
- Design comprehensive training programs
- Set participant limits and schedules
- Track program status (PLANNED, IN_PROGRESS, COMPLETED, CANCELLED)
- Associate trainers and courses with programs

### Enrollment Management
- Enroll employees in training sessions
- Track enrollment status
- Manage participant lists

### Attendance Tracking
- Record attendance for training sessions
- Track present/absent status
- Maintain attendance history

### Authentication & Authorization
- JWT-based authentication
- Secure API endpoints
- Role-based access control

## API Endpoints

| Entity             | Endpoints                              |
|--------------------|----------------------------------------|
| Authentication     | `POST /api/auth/login`                 |
| Employees          | `GET, POST, PUT, DELETE /api/employees`|
| Trainers           | `GET, POST, PUT, DELETE /api/trainers` |
| Courses            | `GET, POST, PUT, DELETE /api/courses`  |
| Training Programs  | `GET, POST, PUT, DELETE /api/training-programs` |
| Training Sessions  | `GET, POST, PUT, DELETE /api/training-sessions` |
| Enrollments        | `GET, POST, PUT, DELETE /api/enrollments` |
| Attendance         | `GET, POST, PUT, DELETE /api/attendance` |

API documentation is available via Swagger UI at `/swagger-ui.html` when the backend is running.

## Data Model

### Core Entities

```
Employee
├── id (UUID)
├── firstName, lastName
├── email
├── department
├── isActive
└── audit fields (createdAt, updatedAt, createdBy, updatedBy)

Trainer
├── id (UUID)
├── firstName, lastName
├── email
├── specialization
├── isActive
└── audit fields

Course
├── id (UUID)
├── title, description
├── startDate, endDate
├── status (DRAFT, PUBLISHED, ARCHIVED)
└── audit fields

TrainingProgram
├── id (UUID)
├── title, description
├── startDate, endDate
├── status (PLANNED, IN_PROGRESS, COMPLETED, CANCELLED)
├── maxParticipants
├── trainerId (FK → Trainer)
├── courseId (FK → Course)
└── audit fields

TrainingSession
├── id (UUID)
├── trainingProgramId (FK → TrainingProgram)
├── sessionDate, startTime, endTime
├── location
└── audit fields

Enrollment
├── id (UUID)
├── employeeId (FK → Employee)
├── trainingSessionId (FK → TrainingSession)
└── audit fields

Attendance
├── id (UUID)
├── enrollmentId (FK → Enrollment)
├── attendedAt
├── present
└── audit fields
```

## Getting Started

### Prerequisites
- Java 21 JDK
- Node.js 18+ and npm
- Docker and Docker Compose
- Git

### Environment Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd training-management-portal
```

2. Copy the environment file:
```bash
copy .env.example .env
```

3. Update `.env` with your configuration (database credentials, JWT secret, etc.)

### Running the Application

#### Start the Database
```bash
docker-compose up -d
```

#### Start the Backend
```bash
cd backend
.\gradlew bootRun
```

The backend will start at `http://localhost:8080`

#### Start the Frontend
```bash
cd frontend
npm install
npm run dev
```

The frontend will start at `http://localhost:5173`

### Running Tests

#### Backend Tests
```bash
cd backend
.\gradlew test
```

#### Frontend Build
```bash
cd frontend
npm run build
```

## Configuration

### Backend Configuration
The application uses profile-based configuration:
- `application-local.properties` - Local development
- `application-dev.properties` - Development environment
- `application-prod.properties` - Production environment
- `application-test.properties` - Testing (uses H2 in-memory database)

### Environment Variables
| Variable                | Description                      | Default     |
|-------------------------|----------------------------------|-------------|
| `DB_HOST`               | Database host                    | localhost   |
| `DB_PORT`               | Database port                    | 5432        |
| `DB_NAME`               | Database name                    | training_portal |
| `DB_USERNAME`           | Database username                | postgres    |
| `DB_PASSWORD`           | Database password                | postgres    |
| `JWT_SECRET`            | JWT signing secret               | (generated) |
| `AUTH_DEFAULT_USERNAME` | Default admin username           | admin       |
| `AUTH_DEFAULT_PASSWORD` | Default admin password           | password    |

## Frontend Pages

| Page                    | Route              | Description                        |
|-------------------------|--------------------|------------------------------------|
| Dashboard               | `/`                | Overview and statistics            |
| Login                   | `/login`           | User authentication                |
| Employees               | `/employees`       | Employee management                |
| Trainers                | `/trainers`        | Trainer management                 |
| Training Programs       | `/training-programs`| Training program management       |
| Assignments             | `/assignments`     | Training assignments               |
| Profile                 | `/profile`         | User profile settings              |
| 404 Not Found           | `*`                | Unknown routes                     |
| 403 Forbidden           | `/forbidden`       | Access denied                      |
| 500 Server Error        | `/server-error`    | Server error page                  |


## Enhancements

This project is fully functional for training management. The following enhancements can make it more scalable and user-friendly.

• Role-based access (Admin, Trainer, Employee)
• Email notifications for enrollments and training schedules
• Attendance reports with PDF/Excel export
• Training completion certificates
• Search, filter, and pagination for all modules
• Dashboard analytics with charts
• Employee profile management
• Training calendar integration
• Audit logs for data changes
• File upload for training materials
• Password reset and email verification
• Soft delete instead of permanent delete
• Unit and integration testing
• API documentation using Swagger
• Deployment to cloud (AWS/Azure)

## Security

The application implements:
- JWT-based stateless authentication
- Password hashing
- CORS configuration for frontend-backend communication
- Role-based access control for API endpoints
- Input validation on all endpoints

## Database Migrations

Database schema is managed by Liquibase. Migration scripts are located at:
```
backend/src/main/resources/db/changelog/
```

Migrations run automatically on application startup.

## License

This project is proprietary software for internal use.

## Contributing

1. Create a feature branch from `main`
2. Make your changes following the existing code style
3. Write tests for new functionality
4. Submit a pull request for review
