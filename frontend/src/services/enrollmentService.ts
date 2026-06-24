import api from './api';
import { Enrollment, PageResponse } from '../types';

const getEnrollments = async (page = 0, size = 10) => {
  const response = await api.get<PageResponse<Enrollment>>('/enrollments', {
    params: { page, size }
  });
  return response.data;
};

const getEnrollment = async (id: string) => {
  const response = await api.get<Enrollment>(`/enrollments/${id}`);
  return response.data;
};

const createEnrollment = async (payload: { employeeId: string; trainingSessionId: string }) => {
  const response = await api.post<Enrollment>('/enrollments', payload);
  return response.data;
};

const updateEnrollment = async (id: string, payload: { employeeId: string; trainingSessionId: string }) => {
  const response = await api.patch<Enrollment>(`/enrollments/${id}`, payload);
  return response.data;
};

const deleteEnrollment = async (id: string) => {
  await api.delete(`/enrollments/${id}`);
};

export default {
  getEnrollments,
  getEnrollment,
  createEnrollment,
  updateEnrollment,
  deleteEnrollment
};
