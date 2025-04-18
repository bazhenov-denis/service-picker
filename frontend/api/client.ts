import axios from 'axios';

const API_URL = 'http://localhost:8080';

export const fetchRegions = async (): Promise<any[]> => {
  try {
    const response = await axios.get(`${API_URL}/regions`);
    return response.data;
  } catch (error) {
    console.error('Ошибка при получении регионов:', error);
    throw error;
  }
};

export const fetchProfessions = async (): Promise<any[]> => {
  try {
    const response = await axios.get(`${API_URL}/professions`);
    return response.data;
  } catch (error) {
    console.error('Ошибка при получении профессий:', error);
    throw error;
  }
};