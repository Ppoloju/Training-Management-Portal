import { useMemo, useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  FormControl,
  IconButton,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  SelectChangeEvent,
  Snackbar,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography
} from '@mui/material';
import { Delete as DeleteIcon } from '@mui/icons-material';
import PageHeader from '../components/PageHeader';
import employeeService from '../services/employeeService';
import trainingProgramService from '../services/trainingProgramService';
import enrollmentService from '../services/enrollmentService';
import trainingSessionService from '../services/trainingSessionService';
import { Employee, PageResponse, TrainingProgram, TrainingSession, Enrollment } from '../types';

const AssignmentsPage = () => {
  const [selectedEmployee, setSelectedEmployee] = useState('');
  const [selectedProgram, setSelectedProgram] = useState('');
  const [selectedSession, setSelectedSession] = useState('');
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
  const queryClient = useQueryClient();

  const { data: employeesData } = useQuery<PageResponse<Employee>>({
    queryKey: ['employees', 0, 100],
    queryFn: () => employeeService.getEmployees(0, 100)
  });
  const { data: programsData } = useQuery<PageResponse<TrainingProgram>>({
    queryKey: ['trainingPrograms', 0, 100],
    queryFn: () => trainingProgramService.getTrainingPrograms(0, 100)
  });
  const { data: sessionsData } = useQuery<PageResponse<TrainingSession>>({
    queryKey: ['trainingSessions', 0, 100],
    queryFn: () => trainingSessionService.getTrainingSessions(0, 100)
  });
  const { data: enrollmentsData } = useQuery<PageResponse<Enrollment>>({
    queryKey: ['enrollments', 0, 100],
    queryFn: () => enrollmentService.getEnrollments(0, 100)
  });

  const programSessions = useMemo(() => {
    if (!selectedProgram || !sessionsData?.content) return [];
    return sessionsData.content.filter((s) => s.trainingProgramId === selectedProgram);
  }, [selectedProgram, sessionsData]);

  const createEnrollmentMutation = useMutation({
    mutationFn: enrollmentService.createEnrollment,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['enrollments'] });
      setSnackbar({ open: true, message: 'Employee assigned successfully', severity: 'success' });
      setSelectedEmployee('');
      setSelectedProgram('');
      setSelectedSession('');
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to assign employee', severity: 'error' });
    }
  });

  const deleteEnrollmentMutation = useMutation({
    mutationFn: enrollmentService.deleteEnrollment,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['enrollments'] });
      setSnackbar({ open: true, message: 'Assignment removed successfully', severity: 'success' });
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to remove assignment', severity: 'error' });
    }
  });

  const handleEmployeeChange = (event: SelectChangeEvent) => {
    setSelectedEmployee(event.target.value);
  };

  const handleProgramChange = (event: SelectChangeEvent) => {
    setSelectedProgram(event.target.value);
    setSelectedSession('');
  };

  const handleSessionChange = (event: SelectChangeEvent) => {
    setSelectedSession(event.target.value);
  };

  const handleAssign = () => {
    if (!selectedEmployee || !selectedSession) return;

    const employee = employeesData?.content.find((e) => e.id === selectedEmployee);
    if (employee && !employee.isActive) {
      setSnackbar({ open: true, message: 'Cannot assign inactive employee', severity: 'error' });
      return;
    }

    const alreadyEnrolled = enrollmentsData?.content.some(
      (e) => e.employeeId === selectedEmployee && e.trainingSessionId === selectedSession
    );
    if (alreadyEnrolled) {
      setSnackbar({ open: true, message: 'Employee is already enrolled in this session', severity: 'error' });
      return;
    }

    createEnrollmentMutation.mutate({
      employeeId: selectedEmployee,
      trainingSessionId: selectedSession
    });
  };

  const handleRemove = (id: string) => {
    if (window.confirm('Remove this assignment?')) {
      deleteEnrollmentMutation.mutate(id);
    }
  };

  const activeEmployees = employeesData?.content.filter((e) => e.isActive) ?? [];

  return (
    <>
      <PageHeader title="Assignments" subtitle="Assign employees to training programs and sessions" />
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Box display="grid" gap={2}>
            <FormControl fullWidth>
              <InputLabel>Employee</InputLabel>
              <Select value={selectedEmployee} label="Employee" onChange={handleEmployeeChange}>
                {activeEmployees.map((employee) => (
                  <MenuItem key={employee.id} value={employee.id}>
                    {`${employee.firstName} ${employee.lastName} (${employee.department})`}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl fullWidth>
              <InputLabel>Training Program</InputLabel>
              <Select value={selectedProgram} label="Training Program" onChange={handleProgramChange}>
                {programsData?.content.map((program) => (
                  <MenuItem key={program.id} value={program.id}>
                    {program.title}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl fullWidth disabled={!selectedProgram}>
              <InputLabel>Training Session</InputLabel>
              <Select value={selectedSession} label="Training Session" onChange={handleSessionChange}>
                {programSessions.length === 0 ? (
                  <MenuItem disabled value="">
                    No sessions for this program
                  </MenuItem>
                ) : (
                  programSessions.map((session) => (
                    <MenuItem key={session.id} value={session.id}>
                      {session.title} ({session.startDate} → {session.endDate})
                    </MenuItem>
                  ))
                )}
              </Select>
            </FormControl>
            <Button
              variant="contained"
              disabled={
                !selectedEmployee ||
                !selectedSession ||
                createEnrollmentMutation.isPending
              }
              onClick={handleAssign}
            >
              Assign Employee
            </Button>
          </Box>
        </CardContent>
      </Card>

      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Current Assignments
          </Typography>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Employee</TableCell>
                  <TableCell>Department</TableCell>
                  <TableCell>Session</TableCell>
                  <TableCell>Program</TableCell>
                  <TableCell>Assigned Date</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {enrollmentsData?.content.length ? (
                  enrollmentsData.content.map((enrollment) => (
                    <TableRow key={enrollment.id}>
                      <TableCell>{enrollment.employeeName || enrollment.employeeId}</TableCell>
                      <TableCell>{enrollment.employeeDepartment || '—'}</TableCell>
                      <TableCell>{enrollment.trainingSessionTitle || enrollment.trainingSessionId}</TableCell>
                      <TableCell>{enrollment.trainingProgramTitle || '—'}</TableCell>
                      <TableCell>
                        {enrollment.enrolledAt ? new Date(enrollment.enrolledAt).toLocaleDateString() : '—'}
                      </TableCell>
                      <TableCell align="right">
                        <IconButton
                          size="small"
                          color="error"
                          onClick={() => handleRemove(enrollment.id)}
                          disabled={deleteEnrollmentMutation.isPending}
                        >
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={6}>No assignments found.</TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </CardContent>
      </Card>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert severity={snackbar.severity} onClose={() => setSnackbar({ ...snackbar, open: false })}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </>
  );
};

export default AssignmentsPage;
