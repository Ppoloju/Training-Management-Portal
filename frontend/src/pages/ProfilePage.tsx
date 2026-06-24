import { Box, Card, CardContent, Typography } from '@mui/material';
import PageHeader from '../components/PageHeader';

const ProfilePage = () => (
  <>
    <PageHeader title="Profile" subtitle="Your account details" />
    <Card>
      <CardContent>
        <Typography variant="h6">Account</Typography>
        <Box mt={2}>
          <Typography>Email: user@example.com</Typography>
          <Typography>Name: Training Portal User</Typography>
        </Box>
      </CardContent>
    </Card>
  </>
);

export default ProfilePage;
