import { Box, Button, Container, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const NotFoundPage = () => {
  const navigate = useNavigate();
  return (
    <Container sx={{ mt: 10, textAlign: 'center' }}>
      <Typography variant="h3" gutterBottom>
        404
      </Typography>
      <Typography variant="h6" gutterBottom>
        Page not found.
      </Typography>
      <Box mt={3}>
        <Button variant="contained" onClick={() => navigate('/')}>Go to dashboard</Button>
      </Box>
    </Container>
  );
};

export default NotFoundPage;
