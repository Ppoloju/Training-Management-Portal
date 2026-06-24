import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  IconButton,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Snackbar,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography
} from '@mui/material';
import { Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import PageHeader from '../components/PageHeader';
import enrollmentService from '../services/enrollmentService';
import employeeService from '../services/employeeService';
import trainingSessionService from '../services/trainingSessionService';
import { Employee, Enrollment, PageResponse, TrainingSession } from '../types';

const INITIAL_FORM = {
  employeeId: '',
  trainingSessionId: ''
};

const EnrollmentsPage = () => {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [editingEnrollment, setEditingEnrollment] = useState<Enrollment | null>(null);
  const [formData, setFormData] = useState(INITIAL_FORM);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
  const queryClient = useQueryClient();

  const { data: employeesData } = useQuery<PageResponse<Employee>>({
    queryKey: ['employees-all'],
    queryFn: () => employeeService.getEmployees(0, 100)
  });

  const { data: sessionsData } = useQuery<PageResponse<TrainingSession>>({
    queryKey: ['trainingSessions-all'],
    queryFn: () => trainingSessionService.getTrainingSessions(0, 100)
  });

  const { data, isLoading } = useQuery<PageResponse<Enrollment>>({
    queryKey: ['enrollments', page],
    queryFn: () => enrollmentService.getEnrollments(page, 10)
  });

  const createMutation = useMutation({
    mutationFn: enrollmentService.createEnrollment,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['enrollments'] });
      setSnackbar({ open: true, message: 'Enrollment created successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to create enrollment', severity: 'error' });
    }
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: { employeeId: string; trainingSessionId: string } }) =>
      enrollmentService.updateEnrollment(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['enrollments'] });
      setSnackbar({ open: true, message: 'Enrollment updated successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to update enrollment', severity: 'error' });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: enrollmentService.deleteEnrollment,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['enrollments'] });
      setSnackbar({ open: true, message: 'Enrollment deleted successfully', severity: 'success' });
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to delete enrollment', severity: 'error' });
    }
  });

  const employees = employeesData?.content ?? [];
  const sessions = sessionsData?.content ?? [];

  const getEmployeeLabel = (id: string) => {
    const emp = employees.find((e) => e.id === id);
    return emp ? `${emp.firstName} ${emp.lastName}` : id;
  };

  const getSessionLabel = (id: string) => {
    const session = sessions.find((s) => s.id === id);
    return session ? session.title : id;
  };

  const filteredEnrollments = data?.content.filter((enrollment) => {
    const term = search.toLowerCase();
    return (
      enrollment.employeeName?.toLowerCase().includes(term) ||
      enrollment.trainingSessionTitle?.toLowerCase().includes(term) ||
      enrollment.trainingProgramTitle?.toLowerCase().includes(term) ||
      getEmployeeLabel(enrollment.employeeId).toLowerCase().includes(term) ||
      getSessionLabel(enrollment.trainingSessionId).toLowerCase().includes(term)
    );
  });

  const handleOpenDialog = (enrollment?: Enrollment) => {
    if (enrollment) {
      setEditingEnrollment(enrollment);
      setFormData({
        employeeId: enrollment.employeeId,
        trainingSessionId: enrollment.trainingSessionId
      });
    } else {
      setEditingEnrollment(null);
      setFormData(INITIAL_FORM);
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingEnrollment(null);
    setFormData(INITIAL_FORM);
  };

  const handleSubmit = () => {
    if (editingEnrollment) {
      updateMutation.mutate({ id: editingEnrollment.id, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const handleDelete = (id: string) => {
    if (window.confirm('Are you sure you want to delete this enrollment?')) {
      deleteMutation.mutate(id);
    }
  };

  return (
    <>
      <PageHeader title="Enrollments" subtitle="Manage employee enrollments in training sessions" />
      <Card>
        <CardContent>
          <Box mb={2} display="flex" justifyContent="space-between" flexWrap="wrap" gap={2}>
            <TextField
              label="Search enrollments"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              size="small"
            />
            <Button variant="contained" onClick={() => handleOpenDialog()}>
              Add Enrollment
            </Button>
          </Box>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Employee</TableCell>
                  <TableCell>Department</TableCell>
                  <TableCell>Session</TableCell>
                  <TableCell>Program</TableCell>
                  <TableCell>Enrolled Date</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {isLoading ? (
                  <TableRow>
                    <TableCell colSpan={6}>Loading...</TableCell>
                  </TableRow>
                ) : filteredEnrollments?.length ? (
                  filteredEnrollments.map((enrollment) => (
                    <TableRow key={enrollment.id}>
                      <TableCell>{enrollment.employeeName || getEmployeeLabel(enrollment.employeeId)}</TableCell>
                      <TableCell>{enrollment.employeeDepartment || '—'}</TableCell>
                      <TableCell>{enrollment.trainingSessionTitle || getSessionLabel(enrollment.trainingSessionId)}</TableCell>
                      <TableCell>{enrollment.trainingProgramTitle || '—'}</TableCell>
                      <TableCell>
                        {enrollment.enrolledAt ? new Date(enrollment.enrolledAt).toLocaleDateString() : '—'}
                      </TableCell>
                      <TableCell align="right">
                        <IconButton size="small" color="primary" onClick={() => handleOpenDialog(enrollment)}>
                          <EditIcon />
                        </IconButton>
                        <IconButton size="small" color="error" onClick={() => handleDelete(enrollment.id)}>
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={6}>No enrollments found.</TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
          <Box mt={2} display="flex" justifyContent="space-between" alignItems="center">
            <Button disabled={page === 0} onClick={() => setPage((p) => Math.max(p - 1, 0))}>
              Previous
            </Button>
            <Typography>
              Page {page + 1} of {data?.totalPages ?? 1}
            </Typography>
            <Button disabled={page >= (data?.totalPages ?? 1) - 1} onClick={() => setPage((p) => p + 1)}>
              Next
            </Button>
          </Box>
        </CardContent>
      </Card>

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>{editingEnrollment ? 'Edit Enrollment' : 'Add Enrollment'}</DialogTitle>
        <DialogContent>
          <Box display="flex" flexDirection="column" gap={2} mt={1}>
            <FormControl fullWidth required>
              <InputLabel>Employee</InputLabel>
              <Select
                label="Employee"
                value={formData.employeeId}
                onChange={(e) => setFormData({ ...formData, employeeId: e.target.value })}
              >
                {employees.map((employee) => (
                  <MenuItem key={employee.id} value={employee.id}>
                    {`${employee.firstName} ${employee.lastName} (${employee.department})`}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl fullWidth required>
              <InputLabel>Training Session</InputLabel>
              <Select
                label="Training Session"
                value={formData.trainingSessionId}
                onChange={(e) => setFormData({ ...formData, trainingSessionId: e.target.value })}
              >
                {sessions.map((session) => (
                  <MenuItem key={session.id} value={session.id}>
                    {session.title}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button
            onClick={handleSubmit}
            variant="contained"
            disabled={
              !formData.employeeId ||
              !formData.trainingSessionId ||
              createMutation.isPending ||
              updateMutation.isPending
            }
          >
            {editingEnrollment ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>

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

export default EnrollmentsPage;
