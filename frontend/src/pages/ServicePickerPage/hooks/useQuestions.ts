import { useState, useEffect } from "react";
import { fetchQuestions } from "../client/httpClient";
import type { Question } from "../types/question";

export const useQuestions = (role: 'client' | 'admin' = 'client') => {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadQuestions = async () => {
      try {
        const data = await fetchQuestions(role);
        setQuestions(data as Question[]);
      } catch (err) {
        console.error("Ошибка при загрузке вопросов:", err);
        setError("Не удалось загрузить вопросы");
      } finally {
        setIsLoading(false);
      }
    };

    loadQuestions();
  }, [role]);

  return { questions, isLoading, error };
};
