import { useState, useEffect } from "react";
import { fetchQuestions } from "../client/httpClient";
import type { Question } from "../types/question";

export const useQuestions = () => {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadQuestions = async () => {
      try {
        const data = await fetchQuestions();
        setQuestions(data as Question[]);
      } catch (err) {
        console.error("Ошибка при загрузке вопросов:", err);
        setError("Не удалось загрузить вопросы");
      } finally {
        setIsLoading(false);
      }
    };

    loadQuestions();
  }, []);

  return { questions, isLoading, error };
};
