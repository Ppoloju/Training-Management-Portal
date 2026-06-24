import api from './api';
import { PageResponse, Trainer } from '../types';

const getTrainers = async (page = 0, size = 10) => {
  const response = await api.get<PageResponse<Trainer>>('/trainers', {
    params: { page, size }
  });
  return response.data;
};

const getTrainer = async (id: string) => {
  const response = await api.get<Trainer>(`/trainers/${id}`);
  return response.data;
};

const createTrainer = async (payload: Omit<Trainer, 'id'>) => {
  const response = await api.post<Trainer>('/trainers', payload);
  return response.data;
};

const updateTrainer = async (id: string, payload: Omit<Trainer, 'id'>) => {
  const response = await api.patch<Trainer>(`/trainers/${id}`, payload);
  return response.data;
};

const activateTrainer = async (id: string) => {
  const response = await api.patch<Trainer>(`/trainers/${id}/activate`);
  return response.data;
};

const deactivateTrainer = async (id: string) => {
  const response = await api.patch<Trainer>(`/trainers/${id}/deactivate`);
  return response.data;
};

const deleteTrainer = async (id: string) => {
  await api.delete(`/trainers/${id}`);
};

export default {
  getTrainers,
  getTrainer,
  createTrainer,
  updateTrainer,
  activateTrainer,
  deactivateTrainer,
  deleteTrainer
};
