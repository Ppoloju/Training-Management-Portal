import api from './api';
import { PageResponse, TrainingProgram } from '../types';

const getTrainingPrograms = async (page = 0, size = 10) => {
  const response = await api.get<PageResponse<TrainingProgram>>('/training-programs', {
    params: { page, size }
  });
  return response.data;
};

const getTrainingProgram = async (id: string) => {
  const response = await api.get<TrainingProgram>(`/training-programs/${id}`);
  return response.data;
};

const createTrainingProgram = async (payload: Omit<TrainingProgram, 'id'>) => {
  const response = await api.post<TrainingProgram>('/training-programs', payload);
  return response.data;
};

const updateTrainingProgram = async (id: string, payload: Omit<TrainingProgram, 'id'>) => {
  const response = await api.patch<TrainingProgram>(`/training-programs/${id}`, payload);
  return response.data;
};

const deleteTrainingProgram = async (id: string) => {
  await api.delete(`/training-programs/${id}`);
};

export default {
  getTrainingPrograms,
  getTrainingProgram,
  createTrainingProgram,
  updateTrainingProgram,
  deleteTrainingProgram
};
