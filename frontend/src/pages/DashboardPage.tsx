import { Grid, Paper, Typography, Box } from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import { People as PeopleIcon, School as SchoolIcon, Assignment as AssignmentIcon, TrendingUp as TrendingUpIcon } from '@mui/icons-material';
import PageHeader from '../components/PageHeader';
import DashboardCard from '../components/DashboardCard';
import employeeService from '../services/employeeService';
import trainerService from '../services/trainerService';
import trainingProgramService from '../services/trainingProgramService';
import { Employee, PageResponse, Trainer, TrainingProgram } from '../types';

const DashboardPage = () => {
  const { data: employeesData } = useQuery<PageResponse<Employee>>({
    queryKey: ['employees', 0, 10],
    queryFn: () => employeeService.getEmployees(0, 10)
  });
  const { data: trainersData } = useQuery<PageResponse<Trainer>>({
    queryKey: ['trainers', 0, 10],
    queryFn: () => trainerService.getTrainers(0, 10)
  });
  const { data: programsData } = useQuery<PageResponse<TrainingProgram>>({
    queryKey: ['trainingPrograms', 0, 10],
    queryFn: () => trainingProgramService.getTrainingPrograms(0, 10)
  });

  return (
    <>
      <PageHeader title="Dashboard" subtitle="Overview of training portal operations" />
      <Grid container spacing={3}>
        <Grid item xs={12} sm={6} md={3}>
          <DashboardCard 
            title="Total Employees" 
            value={employeesData?.totalElements ?? 0} 
            icon={<PeopleIcon />}
            color="primary"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <DashboardCard 
            title="Total Trainers" 
            value={trainersData?.totalElements ?? 0} 
            icon={<SchoolIcon />}
            color="secondary"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <DashboardCard 
            title="Training Programs" 
            value={programsData?.totalElements ?? 0} 
            icon={<AssignmentIcon />}
            color="info"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <DashboardCard 
            title="Active Programs" 
            value={programsData?.content.filter((program) => program.status !== 'COMPLETED').length ?? 0} 
            icon={<TrendingUpIcon />}
            color="success"
          />
        </Grid>
      </Grid>
      <Paper sx={{ mt: 3, p: 3 }}>
        <Typography variant="h6" gutterBottom>
          Recent Programs
        </Typography>
        {programsData?.content.length ? (
          programsData.content.slice(0, 5).map((program) => (
            <Box key={program.id} sx={{ mb: 2, pb: 2, borderBottom: '1px solid #eee' }}>
              <Typography variant="subtitle1" fontWeight="bold">
                {program.title}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {program.startDate} → {program.endDate}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Status: {program.status}
              </Typography>
            </Box>
          ))
        ) : (
          <Typography color="text.secondary">No programs found. Create training programs to get started.</Typography>
        )}
      </Paper>
    </>
  );
};

export default DashboardPage;
