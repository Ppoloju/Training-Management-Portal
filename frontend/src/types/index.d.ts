export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface Employee {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  department: string;
  isActive: boolean;
}

export interface Trainer {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  specialization: string;
  isActive: boolean;
}

export interface TrainingProgram {
  id: string;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  status: string;
  maxParticipants?: number;
  trainerId: string;
  courseId: string;
}

export interface Course {
  id: string;
  title: string;
  description?: string;
  startDate?: string;
  endDate?: string;
  status?: string;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
  updatedBy?: string;
}

export interface Enrollment {
  id: string;
  employeeId: string;
  employeeName?: string;
  employeeDepartment?: string;
  trainingSessionId: string;
  trainingSessionTitle?: string;
  trainingProgramId?: string;
  trainingProgramTitle?: string;
  enrolledAt: string;
  updatedAt?: string;
  createdBy?: string;
  updatedBy?: string;
}

export interface TrainingSession {
  id: string;
  title: string;
  description?: string;
  startDate: string;
  endDate: string;
  trainingProgramId: string;
  location?: string;
  capacity?: number;
}

export interface Attendance {
  id: string;
  enrollmentId: string;
  attendedAt: string;
  present: boolean;
}
