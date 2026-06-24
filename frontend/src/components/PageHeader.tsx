import { Box, Typography } from '@mui/material';

interface PageHeaderProps {
  title: string;
  subtitle?: string;
}

const PageHeader = ({ title, subtitle }: PageHeaderProps) => (
  <Box mb={3}>
    <Typography variant="h4" component="h1" gutterBottom>
      {title}
    </Typography>
    {subtitle ? (
      <Typography color="text.secondary">{subtitle}</Typography>
    ) : null}
  </Box>
);

export default PageHeader;
