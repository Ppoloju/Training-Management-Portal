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
import trainerService from '../services/trainerService';
import { PageResponse, Trainer } from '../types';

const SPECIALIZATIONS = [
  'Java',
  'Spring Boot',
  'Python',
  'React',
  'Angular',
  'DevOps',
  'Cloud (AWS)',
  'Cloud (Azure)',
  'Data Science',
  'Machine Learning',
  'Cybersecurity',
  'Project Management',
  'Agile / Scrum',
  'Soft Skills'
];

const INITIAL_FORM = { firstName: '', lastName: '', email: '', specialization: '', isActive: 'true' };

const TrainersPage = () => {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [editingTrainer, setEditingTrainer] = useState<Trainer | null>(null);
  const [formData, setFormData] = useState(INITIAL_FORM);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery<PageResponse<Trainer>>({
    queryKey: ['trainers', page],
    queryFn: () => trainerService.getTrainers(page, 10)
  });

  const createMutation = useMutation({
    mutationFn: trainerService.createTrainer,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainers'] });
      setSnackbar({ open: true, message: 'Trainer created successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to create trainer', severity: 'error' });
    }
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Omit<Trainer, 'id'> }) =>
      trainerService.updateTrainer(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainers'] });
      setSnackbar({ open: true, message: 'Trainer updated successfully', severity: 'success' });
      handleCloseDialog();
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to update trainer', severity: 'error' });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: trainerService.deleteTrainer,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainers'] });
      setSnackbar({ open: true, message: 'Trainer deleted successfully', severity: 'success' });
    },
    onError: (error: any) => {
      setSnackbar({ open: true, message: error.response?.data?.message || 'Failed to delete trainer', severity: 'error' });
    }
  });

  const filteredTrainers = data?.content.filter(
    (trainer) =>
      trainer.firstName.toLowerCase().includes(search.toLowerCase()) ||
      trainer.lastName.toLowerCase().includes(search.toLowerCase()) ||
      trainer.email.toLowerCase().includes(search.toLowerCase()) ||
      trainer.specialization?.toLowerCase().includes(search.toLowerCase())
  );

  const handleOpenDialog = (trainer?: Trainer) => {
    if (trainer) {
      setEditingTrainer(trainer);
      setFormData({
        firstName: trainer.firstName,
        lastName: trainer.lastName,
        email: trainer.email,
        specialization: trainer.specialization,
        isActive: trainer.isActive ? 'true' : 'false'
      });
    } else {
      setEditingTrainer(null);
      setFormData(INITIAL_FORM);
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingTrainer(null);
    setFormData(INITIAL_FORM);
  };

  const handleSubmit = () => {
    const payload = { ...formData, isActive: formData.isActive === 'true' };
    if (editingTrainer) {
      updateMutation.mutate({ id: editingTrainer.id, data: payload });
    } else {
      createMutation.mutate(payload);
    }
  };

  const handleDelete = (id: string) => {
    if (window.confirm('Are you sure you want to delete this trainer?')) {
      deleteMutation.mutate(id);
    }
  };

  return (
    <>
      <PageHeader title="Trainers" subtitle="Manage trainers, expertise, and availability" />
      <Card>
        <CardContent>
          <Box mb={2} display="flex" justifyContent="space-between" flexWrap="wrap" gap={2}>
            <TextField
              label="Search trainers"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              size="small"
            />
            <Button variant="contained" onClick={() => handleOpenDialog()}>
              Add Trainer
            </Button>
          </Box>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Name</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Specialization</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {isLoading ? (
                  <TableRow>
                    <TableCell colSpan={5}>Loading...</TableCell>
                  </TableRow>
                ) : filteredTrainers?.length ? (
                  filteredTrainers.map((trainer: Trainer) => (
                    <TableRow key={trainer.id}>
                      <TableCell>{`${trainer.firstName} ${trainer.lastName}`}</TableCell>
                      <TableCell>{trainer.email}</TableCell>
                      <TableCell>{trainer.specialization}</TableCell>
                      <TableCell>
                        <Chip
                          label={trainer.isActive ? 'Active' : 'Inactive'}
                          color={trainer.isActive ? 'success' : 'default'}
                          size="small"
                        />
                      </TableCell>
                      <TableCell align="right">
                        <IconButton size="small" color="primary" onClick={() => handleOpenDialog(trainer)}>
                          <EditIcon />
                        </IconButton>
                        <IconButton size="small" color="error" onClick={() => handleDelete(trainer.id)}>
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={5}>No trainers found.</TableCell>
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
        <DialogTitle>{editingTrainer ? 'Edit Trainer' : 'Add Trainer'}</DialogTitle>
        <DialogContent>
          <Box display="flex" flexDirection="column" gap={2} mt={1}>
            <TextField
              label="First Name"
              value={formData.firstName}
              onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
              fullWidth
              required
            />
            <TextField
              label="Last Name"
              value={formData.lastName}
              onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
              fullWidth
              required
            />
            <TextField
              label="Email"
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              fullWidth
              required
            />

            {/* Specialization dropdown */}
            <FormControl fullWidth required>
              <InputLabel>Specialization</InputLabel>
              <Select
                label="Specialization"
                value={formData.specialization}
                onChange={(e) => setFormData({ ...formData, specialization: e.target.value })}
              >
                {SPECIALIZATIONS.map((spec) => (
                  <MenuItem key={spec} value={spec}>
                    {spec}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            {/* Status dropdown */}
            <FormControl fullWidth required>
              <InputLabel>Status</InputLabel>
              <Select
                label="Status"
                value={formData.isActive}
                onChange={(e) => setFormData({ ...formData, isActive: e.target.value })}
              >
                <MenuItem value="true">Active</MenuItem>
                <MenuItem value="false">Inactive</MenuItem>
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
              !formData.firstName ||
              !formData.lastName ||
              !formData.email ||
              !formData.specialization ||
              createMutation.isPending ||
              updateMutation.isPending
            }
          >
            {editingTrainer ? 'Update' : 'Create'}
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

export default TrainersPage;
