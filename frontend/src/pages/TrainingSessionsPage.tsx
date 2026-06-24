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
import trainingSessionService from '../services/trainingSessionService';
import trainingProgramService from '../services/trainingProgramService';
import { PageResponse, TrainingProgram, TrainingSession } from '../types';

const INITIAL_FORM = {
  title: '',
  description: '',
  startDate: '',
  endDate: '',
  trainingProgramId: '',
  location: '',
  capacity: ''
};

const TrainingSessionsPage = () => {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [editingSession, setEditingSession] = useState<TrainingSession | null>(null);
  const [formData, setFormData] = useState(INITIAL_FORM);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
  const queryClient = useQueryClient();

  const { data: programsData } = useQuery<PageResponse<TrainingProgram>>({
    queryKey: ['trainingPrograms-all'],
    queryFn: () => trainingProgramService.getTrainingPrograms(0, 100)
  });

  const { data, isLoading } = useQuery<PageResponse<TrainingSession>>({
    queryKey: ['trainingSessions', page],
    queryFn: () => trainingSessionService.getTrainingSessions(page, 10)
  });

  const createMutation = useMutation({
    mutationFn: trainingSessionService.createTrainingSession,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainingSessions'] });
      setSnackbar({ open: true, message: 'Training session created successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to create session', severity: 'error' });
    }
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Omit<TrainingSession, 'id'> }) =>
      trainingSessionService.updateTrainingSession(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainingSessions'] });
      setSnackbar({ open: true, message: 'Training session updated successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to update session', severity: 'error' });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: trainingSessionService.deleteTrainingSession,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainingSessions'] });
      setSnackbar({ open: true, message: 'Training session deleted successfully', severity: 'success' });
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to delete session', severity: 'error' });
    }
  });

  const programs = programsData?.content ?? [];

  const getProgramTitle = (id: string) => {
    const program = programs.find((p) => p.id === id);
    return program ? program.title : id;
  };

  const filteredSessions = data?.content.filter(
    (session) =>
      session.title.toLowerCase().includes(search.toLowerCase()) ||
      session.location?.toLowerCase().includes(search.toLowerCase()) ||
      getProgramTitle(session.trainingProgramId).toLowerCase().includes(search.toLowerCase())
  );

  const handleOpenDialog = (session?: TrainingSession) => {
    if (session) {
      setEditingSession(session);
      setFormData({
        title: session.title,
        description: session.description || '',
        startDate: session.startDate,
        endDate: session.endDate,
        trainingProgramId: session.trainingProgramId,
        location: session.location || '',
        capacity: session.capacity?.toString() || ''
      });
    } else {
      setEditingSession(null);
      setFormData(INITIAL_FORM);
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingSession(null);
    setFormData(INITIAL_FORM);
  };

  const handleSubmit = () => {
    const payload = {
      ...formData,
      capacity: formData.capacity ? parseInt(formData.capacity) : undefined
    };
    if (editingSession) {
      updateMutation.mutate({ id: editingSession.id, data: payload });
    } else {
      createMutation.mutate(payload);
    }
  };

  const handleDelete = (id: string) => {
    if (window.confirm('Are you sure you want to delete this training session?')) {
      deleteMutation.mutate(id);
    }
  };

  return (
    <>
      <PageHeader title="Training Sessions" subtitle="Schedule and manage training sessions" />
      <Card>
        <CardContent>
          <Box mb={2} display="flex" justifyContent="space-between" flexWrap="wrap" gap={2}>
            <TextField
              label="Search sessions"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              size="small"
            />
            <Button variant="contained" onClick={() => handleOpenDialog()}>
              Add Session
            </Button>
          </Box>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Title</TableCell>
                  <TableCell>Program</TableCell>
                  <TableCell>Location</TableCell>
                  <TableCell>Capacity</TableCell>
                  <TableCell>Dates</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {isLoading ? (
                  <TableRow>
                    <TableCell colSpan={6}>Loading...</TableCell>
                  </TableRow>
                ) : filteredSessions?.length ? (
                  filteredSessions.map((session) => (
                    <TableRow key={session.id}>
                      <TableCell>{session.title}</TableCell>
                      <TableCell>{getProgramTitle(session.trainingProgramId)}</TableCell>
                      <TableCell>{session.location || '—'}</TableCell>
                      <TableCell>{session.capacity ?? '—'}</TableCell>
                      <TableCell>{`${session.startDate} → ${session.endDate}`}</TableCell>
                      <TableCell align="right">
                        <IconButton size="small" color="primary" onClick={() => handleOpenDialog(session)}>
                          <EditIcon />
                        </IconButton>
                        <IconButton size="small" color="error" onClick={() => handleDelete(session.id)}>
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={6}>No training sessions found.</TableCell>
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
        <DialogTitle>{editingSession ? 'Edit Training Session' : 'Add Training Session'}</DialogTitle>
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
            <FormControl fullWidth required>
              <InputLabel>Training Program</InputLabel>
              <Select
                label="Training Program"
                value={formData.trainingProgramId}
                onChange={(e) => setFormData({ ...formData, trainingProgramId: e.target.value })}
              >
                {programs.length === 0 ? (
                  <MenuItem disabled value="">
                    No programs available
                  </MenuItem>
                ) : (
                  programs.map((program) => (
                    <MenuItem key={program.id} value={program.id}>
                      {program.title}
                    </MenuItem>
                  ))
                )}
              </Select>
            </FormControl>
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
            <TextField
              label="Location"
              value={formData.location}
              onChange={(e) => setFormData({ ...formData, location: e.target.value })}
              fullWidth
            />
            <TextField
              label="Capacity"
              type="number"
              value={formData.capacity}
              onChange={(e) => setFormData({ ...formData, capacity: e.target.value })}
              fullWidth
              inputProps={{ min: 1 }}
            />
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
              !formData.trainingProgramId ||
              createMutation.isPending ||
              updateMutation.isPending
            }
          >
            {editingSession ? 'Update' : 'Create'}
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

export default TrainingSessionsPage;
