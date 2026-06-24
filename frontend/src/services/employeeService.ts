import api from './api';
import { PageResponse, Employee } from '../types';

const getEmployees = async (page = 0, size = 10) => {
  const response = await api.get<PageResponse<Employee>>('/employees', {
    params: { page, size }
  });
  return response.data;
};

const getEmployee = async (id: string) => {
  const response = await api.get<Employee>(`/employees/${id}`);
  return response.data;
};

const createEmployee = async (payload: Omit<Employee, 'id'>) => {
  const response = await api.post<Employee>('/employees', payload);
  return response.data;
};

const updateEmployee = async (id: string, payload: Omit<Employee, 'id'>) => {
  const response = await api.patch<Employee>(`/employees/${id}`, payload);
  return response.data;
};

const activateEmployee = async (id: string) => {
  const response = await api.patch<Employee>(`/employees/${id}/activate`);
  return response.data;
};

const deactivateEmployee = async (id: string) => {
  const response = await api.patch<Employee>(`/employees/${id}/deactivate`);
  return response.data;
};

const deleteEmployee = async (id: string) => {
  await api.delete(`/employees/${id}`);
};

export default {
  getEmployees,
  getEmployee,
  createEmployee,
  updateEmployee,
  activateEmployee,
  deactivateEmployee,
  deleteEmployee
};
