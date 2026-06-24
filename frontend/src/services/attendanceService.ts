import api from './api';
import { Attendance, PageResponse } from '../types';

const getAttendances = async (page = 0, size = 10) => {
  const response = await api.get<PageResponse<Attendance>>('/attendances', {
    params: { page, size }
  });
  return response.data;
};

const getAttendance = async (id: string) => {
  const response = await api.get<Attendance>(`/attendances/${id}`);
  return response.data;
};

const createAttendance = async (payload: { enrollmentId: string; attendedAt: string; present: boolean }) => {
  const response = await api.post<Attendance>('/attendances', payload);
  return response.data;
};

const updateAttendance = async (id: string, payload: { enrollmentId: string; attendedAt: string; present: boolean }) => {
  const response = await api.patch<Attendance>(`/attendances/${id}`, payload);
  return response.data;
};

const deleteAttendance = async (id: string) => {
  await api.delete(`/attendances/${id}`);
};

export default {
  getAttendances,
  getAttendance,
  createAttendance,
  updateAttendance,
  deleteAttendance
};
