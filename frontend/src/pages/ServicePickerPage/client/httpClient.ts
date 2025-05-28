import axios from "axios";
import type { OfferDto } from "../types/service";

const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
  withCredentials: true,
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      console.error("Ошибка сервера:", {
        status: error.response.status,
        data: error.response.data,
      });
    } else if (error.request) {
      console.error("Ошибка сети:", error.message);
    } else {
      console.error("Ошибка:", error.message);
    }
    return Promise.reject(error);
  },
);

export const fetchRegions = async (): Promise<any[]> => {
  try {
    const response = await api.get("/areas");
    return response.data;
  } catch (error) {
    console.error("Ошибка при получении регионов:", error);
    throw error;
  }
};

export const fetchProfessions = async (): Promise<any[]> => {
  try {
    const response = await api.get("/professions");
    return response.data;
  } catch (error) {
    console.error("Ошибка при получении профессий:", error);
    throw error;
  }
};

export const fetchQuestions = async (): Promise<any[]> => {
  try {
    const response = await api.get("/questions");
    return response.data;
  } catch (error) {
    console.error("Ошибка при получении вопросов:", error);
    throw error;
  }
};

type AnswerValue = string[];

export const sendAnswers = async (
  answers: Record<string, AnswerValue>,
): Promise<OfferDto> => {
  // Преобразуем данные в формат, ожидаемый бэкендом
  const formattedData = {
    professionId: parseInt(answers["2"][0]),
    areaId: parseInt(answers["1"][0].split(".")[1]),
    amount: parseInt(answers["3"][0])
  };

  const response = await fetch("http://localhost:8080/service-offer", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(formattedData),
  });

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }

  return response.json();
};
