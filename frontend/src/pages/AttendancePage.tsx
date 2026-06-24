import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
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
import attendanceService from '../services/attendanceService';
import enrollmentService from '../services/enrollmentService';
import { Attendance, Enrollment, PageResponse } from '../types';

const toDatetimeLocalValue = (date: Date) => {
  const pad = (value: number) => String(value).padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
};

const fromApiToDatetimeLocal = (value: string) => {
  const normalized = value.replace(' ', 'T').split('.')[0];
  return normalized.slice(0, 16);
};

const toLocalDateTimePayload = (datetimeLocal: string) =>
  datetimeLocal.length === 16 ? `${datetimeLocal}:00` : datetimeLocal;

const formatLocalDateTime = (value: string) => {
  const normalized = value.replace(' ', 'T').split('.')[0];
  const [datePart, timePart = '00:00:00'] = normalized.split('T');
  const [year, month, day] = datePart.split('-').map(Number);
  const [hour, minute, second = 0] = timePart.split(':').map(Number);
  return new Date(year, month - 1, day, hour, minute, second).toLocaleString();
};

const INITIAL_FORM = {
  enrollmentId: '',
  attendedAt: toDatetimeLocalValue(new Date()),
  present: 'true'
};

const AttendancePage = () => {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [editingAttendance, setEditingAttendance] = useState<Attendance | null>(null);
  const [formData, setFormData] = useState(INITIAL_FORM);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
  const queryClient = useQueryClient();

  const { data: enrollmentsData } = useQuery<PageResponse<Enrollment>>({
    queryKey: ['enrollments-all'],
    queryFn: () => enrollmentService.getEnrollments(0, 100)
  });

  const { data, isLoading } = useQuery<PageResponse<Attendance>>({
    queryKey: ['attendances', page],
    queryFn: () => attendanceService.getAttendances(page, 10)
  });

  const createMutation = useMutation({
    mutationFn: attendanceService.createAttendance,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['attendances'] });
      setSnackbar({ open: true, message: 'Attendance recorded successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to record attendance', severity: 'error' });
    }
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: { enrollmentId: string; attendedAt: string; present: boolean } }) =>
      attendanceService.updateAttendance(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['attendances'] });
      setSnackbar({ open: true, message: 'Attendance updated successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to update attendance', severity: 'error' });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: attendanceService.deleteAttendance,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['attendances'] });
      setSnackbar({ open: true, message: 'Attendance deleted successfully', severity: 'success' });
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to delete attendance', severity: 'error' });
    }
  });

  const enrollments = enrollmentsData?.content ?? [];

  const getEnrollmentLabel = (id: string) => {
    const enrollment = enrollments.find((e) => e.id === id);
    if (!enrollment) return id;
    const employee = enrollment.employeeName || enrollment.employeeId;
    const session = enrollment.trainingSessionTitle || enrollment.trainingSessionId;
    return `${employee} — ${session}`;
  };

  const filteredAttendances = data?.content.filter((attendance) => {
    const term = search.toLowerCase();
    return (
      getEnrollmentLabel(attendance.enrollmentId).toLowerCase().includes(term) ||
      (attendance.present ? 'present' : 'absent').includes(term)
    );
  });

  const handleOpenDialog = (attendance?: Attendance) => {
    if (attendance) {
      setEditingAttendance(attendance);
      setFormData({
        enrollmentId: attendance.enrollmentId,
        attendedAt: attendance.attendedAt ? fromApiToDatetimeLocal(attendance.attendedAt) : toDatetimeLocalValue(new Date()),
        present: attendance.present ? 'true' : 'false'
      });
    } else {
      setEditingAttendance(null);
      setFormData(INITIAL_FORM);
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingAttendance(null);
    setFormData(INITIAL_FORM);
  };

  const handleSubmit = () => {
    const payload = {
      enrollmentId: formData.enrollmentId,
      attendedAt: toLocalDateTimePayload(formData.attendedAt),
      present: formData.present === 'true'
    };
    if (editingAttendance) {
      updateMutation.mutate({ id: editingAttendance.id, data: payload });
    } else {
      createMutation.mutate(payload);
    }
  };

  const handleDelete = (id: string) => {
    if (window.confirm('Are you sure you want to delete this attendance record?')) {
      deleteMutation.mutate(id);
    }
  };

  return (
    <>
      <PageHeader title="Attendance" subtitle="Record and manage session attendance" />
      <Card>
        <CardContent>
          <Box mb={2} display="flex" justifyContent="space-between" flexWrap="wrap" gap={2}>
            <TextField
              label="Search attendance"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              size="small"
            />
            <Button variant="contained" onClick={() => handleOpenDialog()}>
              Record Attendance
            </Button>
          </Box>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Enrollment</TableCell>
                  <TableCell>Attended At</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {isLoading ? (
                  <TableRow>
                    <TableCell colSpan={4}>Loading...</TableCell>
                  </TableRow>
                ) : filteredAttendances?.length ? (
                  filteredAttendances.map((attendance) => (
                    <TableRow key={attendance.id}>
                      <TableCell>{getEnrollmentLabel(attendance.enrollmentId)}</TableCell>
                      <TableCell>{formatLocalDateTime(attendance.attendedAt)}</TableCell>
                      <TableCell>
                        <Chip
                          label={attendance.present ? 'Present' : 'Absent'}
                          color={attendance.present ? 'success' : 'default'}
                          size="small"
                        />
                      </TableCell>
                      <TableCell align="right">
                        <IconButton size="small" color="primary" onClick={() => handleOpenDialog(attendance)}>
                          <EditIcon />
                        </IconButton>
                        <IconButton size="small" color="error" onClick={() => handleDelete(attendance.id)}>
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={4}>No attendance records found.</TableCell>
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
        <DialogTitle>{editingAttendance ? 'Edit Attendance' : 'Record Attendance'}</DialogTitle>
        <DialogContent>
          <Box display="flex" flexDirection="column" gap={2} mt={1}>
            <FormControl fullWidth required>
              <InputLabel>Enrollment</InputLabel>
              <Select
                label="Enrollment"
                value={formData.enrollmentId}
                onChange={(e) => setFormData({ ...formData, enrollmentId: e.target.value })}
              >
                {enrollments.length === 0 ? (
                  <MenuItem disabled value="">
                    No enrollments available
                  </MenuItem>
                ) : (
                  enrollments.map((enrollment) => (
                    <MenuItem key={enrollment.id} value={enrollment.id}>
                      {getEnrollmentLabel(enrollment.id)}
                    </MenuItem>
                  ))
                )}
              </Select>
            </FormControl>
            <TextField
              label="Attended At"
              type="datetime-local"
              value={formData.attendedAt}
              onChange={(e) => setFormData({ ...formData, attendedAt: e.target.value })}
              fullWidth
              required
              InputLabelProps={{ shrink: true }}
            />
            <FormControl fullWidth required>
              <InputLabel>Status</InputLabel>
              <Select
                label="Status"
                value={formData.present}
                onChange={(e) => setFormData({ ...formData, present: e.target.value })}
              >
                <MenuItem value="true">Present</MenuItem>
                <MenuItem value="false">Absent</MenuItem>
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
              !formData.enrollmentId ||
              !formData.attendedAt ||
              createMutation.isPending ||
              updateMutation.isPending
            }
          >
            {editingAttendance ? 'Update' : 'Create'}
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

export default AttendancePage;
