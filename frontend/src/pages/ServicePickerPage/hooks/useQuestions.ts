import { useState, useEffect } from "react";
import { fetchQuestions } from "../client/httpClient";

interface Question {
  id: number;
  questionText: string;
  type: string;
  isRequired: boolean;
  referenceType?: string;
  options?: { id: number; text: string }[];
}

export const useQuestions = () => {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadQuestions = async () => {
      try {
        setLoading(true);
        const data = await fetchQuestions();
        setQuestions(data);
      } catch (err) {
        console.error("Ошибка при загрузке вопросов:", err);
        setError("Не удалось загрузить вопросы");
      } finally {
        setLoading(false);
      }
    };

    loadQuestions();
  }, []);

  return { questions, loading, error };
};
