import api from './api';

interface LoginPayload {
  username: string;
  password: string;
}

interface LoginResponse {
  token: string;
}

const login = async (payload: LoginPayload): Promise<LoginResponse> => {
  const response = await api.post<LoginResponse>('/auth/login', payload);
  return response.data;
};

export default { login };
