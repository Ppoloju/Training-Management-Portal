import api from './api';
import { PageResponse, TrainingSession } from '../types';

const getTrainingSessions = async (page = 0, size = 10) => {
  const response = await api.get<PageResponse<TrainingSession>>('/training-sessions', {
    params: { page, size }
  });
  return response.data;
};

const getTrainingSession = async (id: string) => {
  const response = await api.get<TrainingSession>(`/training-sessions/${id}`);
  return response.data;
};

const createTrainingSession = async (payload: Omit<TrainingSession, 'id'>) => {
  const response = await api.post<TrainingSession>('/training-sessions', payload);
  return response.data;
};

const updateTrainingSession = async (id: string, payload: Omit<TrainingSession, 'id'>) => {
  const response = await api.patch<TrainingSession>(`/training-sessions/${id}`, payload);
  return response.data;
};

const deleteTrainingSession = async (id: string) => {
  await api.delete(`/training-sessions/${id}`);
};

export default {
  getTrainingSessions,
  getTrainingSession,
  createTrainingSession,
  updateTrainingSession,
  deleteTrainingSession
};
