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
import trainingProgramService from '../services/trainingProgramService';
import trainerService from '../services/trainerService';
import courseService from '../services/courseService';
import { PageResponse, Trainer, Course, TrainingProgram } from '../types';

type ProgramStatus = 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

const STATUS_OPTIONS: { value: ProgramStatus; label: string }[] = [
  { value: 'PLANNED', label: 'Planned' },
  { value: 'IN_PROGRESS', label: 'In Progress' },
  { value: 'COMPLETED', label: 'Completed' },
  { value: 'CANCELLED', label: 'Cancelled' }
];

const STATUS_COLORS: Record<ProgramStatus, 'default' | 'primary' | 'success' | 'error' | 'warning'> = {
  PLANNED: 'primary',
  IN_PROGRESS: 'warning',
  COMPLETED: 'success',
  CANCELLED: 'error'
};

const INITIAL_FORM = {
  title: '',
  description: '',
  startDate: '',
  endDate: '',
  status: 'PLANNED' as ProgramStatus,
  maxParticipants: '',
  trainerId: '',
  courseId: ''
};

const TrainingProgramsPage = () => {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [editingProgram, setEditingProgram] = useState<TrainingProgram | null>(null);
  const [formData, setFormData] = useState(INITIAL_FORM);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
  const queryClient = useQueryClient();

  // Load all trainers for dropdown (fetch up to 100)
  const { data: trainersData } = useQuery<PageResponse<Trainer>>({
    queryKey: ['trainers-all'],
    queryFn: () => trainerService.getTrainers(0, 100)
  });

  // Load all courses for dropdown (fetch up to 100)
  const { data: coursesData } = useQuery<PageResponse<Course>>({
    queryKey: ['courses-all'],
    queryFn: () => courseService.getCourses(0, 100)
  });

  const { data, isLoading } = useQuery<PageResponse<TrainingProgram>>({
    queryKey: ['trainingPrograms', page],
    queryFn: () => trainingProgramService.getTrainingPrograms(page, 10)
  });

  const createMutation = useMutation({
    mutationFn: trainingProgramService.createTrainingProgram,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainingPrograms'] });
      setSnackbar({ open: true, message: 'Training program created successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to create training program', severity: 'error' });
    }
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Omit<TrainingProgram, 'id'> }) =>
      trainingProgramService.updateTrainingProgram(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainingPrograms'] });
      setSnackbar({ open: true, message: 'Training program updated successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to update training program', severity: 'error' });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: trainingProgramService.deleteTrainingProgram,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainingPrograms'] });
      setSnackbar({ open: true, message: 'Training program deleted successfully', severity: 'success' });
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to delete training program', severity: 'error' });
    }
  });

  const trainers = trainersData?.content ?? [];
  const courses = coursesData?.content ?? [];

  const getTrainerName = (id: string) => {
    const t = trainers.find((tr) => tr.id === id);
    return t ? `${t.firstName} ${t.lastName}` : id;
  };

  const getCourseName = (id: string) => {
    const c = courses.find((co) => co.id === id);
    return c ? c.title : id;
  };

  const filteredPrograms = data?.content.filter(
    (program) =>
      program.title.toLowerCase().includes(search.toLowerCase()) ||
      program.description?.toLowerCase().includes(search.toLowerCase()) ||
      program.status?.toLowerCase().includes(search.toLowerCase())
  );

  const handleOpenDialog = (program?: TrainingProgram) => {
    if (program) {
      setEditingProgram(program);
      setFormData({
        title: program.title,
        description: program.description || '',
        startDate: program.startDate,
        endDate: program.endDate,
        status: (program.status as ProgramStatus) || 'PLANNED',
        maxParticipants: program.maxParticipants?.toString() || '',
        trainerId: program.trainerId,
        courseId: program.courseId
      });
    } else {
      setEditingProgram(null);
      setFormData(INITIAL_FORM);
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingProgram(null);
    setFormData(INITIAL_FORM);
  };

  const handleSubmit = () => {
    const payload = {
      ...formData,
      maxParticipants: formData.maxParticipants ? parseInt(formData.maxParticipants) : undefined
    };
    if (editingProgram) {
      updateMutation.mutate({ id: editingProgram.id, data: payload });
    } else {
      createMutation.mutate(payload);
    }
  };

  const handleDelete = (id: string) => {
    if (window.confirm('Are you sure you want to delete this training program?')) {
      deleteMutation.mutate(id);
    }
  };

  return (
    <>
      <PageHeader title="Training Programs" subtitle="Create, update, and monitor training programs" />
      <Card>
        <CardContent>
          <Box mb={2} display="flex" justifyContent="space-between" flexWrap="wrap" gap={2}>
            <TextField
              label="Search programs"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              size="small"
            />
            <Button variant="contained" onClick={() => handleOpenDialog()}>
              Add Program
            </Button>
          </Box>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Title</TableCell>
                  <TableCell>Trainer</TableCell>
                  <TableCell>Course</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell>Dates</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {isLoading ? (
                  <TableRow>
                    <TableCell colSpan={6}>Loading...</TableCell>
                  </TableRow>
                ) : filteredPrograms?.length ? (
                  filteredPrograms.map((program: TrainingProgram) => (
                    <TableRow key={program.id}>
                      <TableCell>{program.title}</TableCell>
                      <TableCell>{getTrainerName(program.trainerId)}</TableCell>
                      <TableCell>{getCourseName(program.courseId)}</TableCell>
                      <TableCell>
                        <Chip
                          label={STATUS_OPTIONS.find((s) => s.value === program.status)?.label ?? program.status}
                          color={STATUS_COLORS[program.status as ProgramStatus] ?? 'default'}
                          size="small"
                        />
                      </TableCell>
                      <TableCell>{`${program.startDate} → ${program.endDate}`}</TableCell>
                      <TableCell align="right">
                        <IconButton size="small" color="primary" onClick={() => handleOpenDialog(program)}>
                          <EditIcon />
                        </IconButton>
                        <IconButton size="small" color="error" onClick={() => handleDelete(program.id)}>
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={6}>No programs found.</TableCell>
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
            <Button
              disabled={page >= (data?.totalPages ?? 1) - 1}
              onClick={() => setPage((p) => p + 1)}
            >
              Next
            </Button>
          </Box>
        </CardContent>
      </Card>

      {/* Create / Edit Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>{editingProgram ? 'Edit Training Program' : 'Add Training Program'}</DialogTitle>
        <DialogContent>
          <Box display="flex" flexDirection="column" gap={2} mt={1}>
            <TextField
              label="Title"
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
              fullWidth
              required
            />
            <TextField
              label="Description"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              fullWidth
              multiline
              rows={3}
            />
            <TextField
              label="Start Date"
              type="date"
              value={formData.startDate}
              onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
              fullWidth
              required
              InputLabelProps={{ shrink: true }}
            />
            <TextField
              label="End Date"
              type="date"
              value={formData.endDate}
              onChange={(e) => setFormData({ ...formData, endDate: e.target.value })}
              fullWidth
              required
              InputLabelProps={{ shrink: true }}
            />

            {/* Status dropdown */}
            <FormControl fullWidth required>
              <InputLabel>Status</InputLabel>
              <Select
                label="Status"
                value={formData.status}
                onChange={(e) => setFormData({ ...formData, status: e.target.value as ProgramStatus })}
              >
                {STATUS_OPTIONS.map((opt) => (
                  <MenuItem key={opt.value} value={opt.value}>
                    {opt.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <TextField
              label="Max Participants"
              type="number"
              value={formData.maxParticipants}
              onChange={(e) => setFormData({ ...formData, maxParticipants: e.target.value })}
              fullWidth
              inputProps={{ min: 1 }}
            />

            {/* Trainer dropdown */}
            <FormControl fullWidth required>
              <InputLabel>Trainer</InputLabel>
              <Select
                label="Trainer"
                value={formData.trainerId}
                onChange={(e) => setFormData({ ...formData, trainerId: e.target.value })}
              >
                {trainers.length === 0 ? (
                  <MenuItem disabled value="">
                    No trainers available
                  </MenuItem>
                ) : (
                  trainers.map((trainer) => (
                    <MenuItem key={trainer.id} value={trainer.id}>
                      {`${trainer.firstName} ${trainer.lastName}`}
                    </MenuItem>
                  ))
                )}
              </Select>
            </FormControl>

            {/* Course dropdown */}
            <FormControl fullWidth required>
              <InputLabel>Course</InputLabel>
              <Select
                label="Course"
                value={formData.courseId}
                onChange={(e) => setFormData({ ...formData, courseId: e.target.value })}
              >
                {courses.length === 0 ? (
                  <MenuItem disabled value="">
                    No courses available
                  </MenuItem>
                ) : (
                  courses.map((course) => (
                    <MenuItem key={course.id} value={course.id}>
                      {course.title}
                    </MenuItem>
                  ))
                )}
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
              !formData.title ||
              !formData.startDate ||
              !formData.endDate ||
              !formData.trainerId ||
              !formData.courseId ||
              createMutation.isPending ||
              updateMutation.isPending
            }
          >
            {editingProgram ? 'Update' : 'Create'}
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

export default TrainingProgramsPage;
