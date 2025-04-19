import axios from 'axios';

// Создаем экземпляр axios с базовым URL и настройками
const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  // Включаем поддержку CORS
  withCredentials: false
});

// Перехватчик ответов для обработки ошибок
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response) {
      // Ошибка от сервера
      console.error('Ошибка сервера:', {
        status: error.response.status,
        data: error.response.data
      });
    } else if (error.request) {
      // Ошибка сети
      console.error('Ошибка сети:', error.message);
    } else {
      // Другие ошибки
      console.error('Ошибка:', error.message);
    }
    return Promise.reject(error);
  }
);

export const fetchRegions = async (): Promise<any[]> => {
  try {
    const response = await api.get('/areas');
    return response.data;
  } catch (error) {
    console.error('Ошибка при получении регионов:', error);
    throw error;
  }
};

export const fetchProfessions = async (): Promise<any[]> => {
  try {
    const response = await api.get('/professions');
    return response.data;
  } catch (error) {
    console.error('Ошибка при получении профессий:', error);
    throw error;
  }
};