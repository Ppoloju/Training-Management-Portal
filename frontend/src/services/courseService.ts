import api from './api';
import { Course, PageResponse } from '../types';

const getCourses = async (page = 0, size = 100) => {
  const response = await api.get<PageResponse<Course>>('/courses', {
    params: { page, size }
  });
  return response.data;
};

const getCourse = async (id: string) => {
  const response = await api.get<Course>(`/courses/${id}`);
  return response.data;
};

const createCourse = async (payload: Omit<Course, 'id'>) => {
  const response = await api.post<Course>('/courses', payload);
  return response.data;
};

const updateCourse = async (id: string, payload: Omit<Course, 'id'>) => {
  const response = await api.patch<Course>(`/courses/${id}`, payload);
  return response.data;
};

const deleteCourse = async (id: string) => {
  await api.delete(`/courses/${id}`);
};

export default {
  getCourses,
  getCourse,
  createCourse,
  updateCourse,
  deleteCourse
};
