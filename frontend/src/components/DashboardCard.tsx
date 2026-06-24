import { Card, CardContent, Typography, Box, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';

interface DashboardCardProps {
  title: string;
  value: string | number;
  description?: string;
  icon?: React.ReactNode;
  color?: 'primary' | 'secondary' | 'success' | 'warning' | 'info' | 'error';
}

const DashboardCard = ({ title, value, description, icon, color = 'primary' }: DashboardCardProps) => {
  const theme = useTheme();
  
  const colorMap = {
    primary: theme.palette.primary.main,
    secondary: theme.palette.secondary.main,
    success: theme.palette.success?.main || '#10b981',
    warning: theme.palette.warning?.main || '#f59e0b',
    info: theme.palette.info?.main || '#06b6d4',
    error: theme.palette.error?.main || '#ef4444',
  };

  const cardColor = colorMap[color];

  return (
    <Card
      sx={{
        height: '100%',
        transition: 'transform 0.2s, box-shadow 0.2s',
        '&:hover': {
          transform: 'translateY(-4px)',
          boxShadow: theme.palette.mode === 'dark' 
            ? '0 12px 40px rgba(0,0,0,0.5)' 
            : '0 12px 40px rgba(0,0,0,0.12)',
        },
        position: 'relative',
        overflow: 'hidden',
      }}
    >
      <Box
        sx={{
          position: 'absolute',
          top: -20,
          right: -20,
          width: 100,
          height: 100,
          borderRadius: '50%',
          background: alpha(cardColor, 0.1),
        }}
      />
      <CardContent sx={{ position: 'relative', zIndex: 1 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
          <Typography variant="subtitle2" color="text.secondary" fontWeight={600}>
            {title}
          </Typography>
          {icon && (
            <Box
              sx={{
                p: 1,
                borderRadius: 2,
                bgcolor: alpha(cardColor, 0.1),
                color: cardColor,
              }}
            >
              {icon}
            </Box>
          )}
        </Box>
        <Typography variant="h3" component="div" fontWeight={700} sx={{ mb: 1 }}>
          {value}
        </Typography>
        {description && (
          <Typography color="text.secondary" variant="body2">
            {description}
          </Typography>
        )}
      </CardContent>
    </Card>
  );
};

export default DashboardCard;
