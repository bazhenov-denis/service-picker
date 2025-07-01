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

export const fetchQuestions = async (
  role: "client" | "admin" = "client",
): Promise<any[]> => {
  try {
    const params = role === 'client' ? {} : { role };
    const response = await api.get("/questions", { params });
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
  try {
    const response = await api.post("/service-offer", answers);
    return response.data;
  } catch (error) {
    console.error("Ошибка при отправке ответов:", error);
    throw error;
  }
};

export const saveQuestionsOrder = async (modifiableQuestionDTOList: { id: number; position: number; isActive: boolean }[]) => {
  try {
    const response = await api.put("/questions", { modifiableQuestionDTOList });
    return response.data;
  } catch (error) {
    console.error("Ошибка при сохранении порядка/активности вопросов:", error);
    throw error;
  }
};
