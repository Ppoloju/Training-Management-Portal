import { Box, Button, Container, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const ServerErrorPage = () => {
  const navigate = useNavigate();
  return (
    <Container sx={{ mt: 10, textAlign: 'center' }}>
      <Typography variant="h3" gutterBottom>
        500
      </Typography>
      <Typography variant="h6" gutterBottom>
        Something went wrong on the server.
      </Typography>
      <Box mt={3}>
        <Button variant="contained" onClick={() => navigate('/')}>Go to dashboard</Button>
      </Box>
    </Container>
  );
};

export default ServerErrorPage;
