import { useState, useEffect } from "react";
import { useQuestions } from "./useQuestions";
import type { Question } from "../types/question";

export function useAdminQuestions() {
  const { questions } = useQuestions("admin");
  const [adminQuestions, setAdminQuestions] = useState<Question[]>(
    questions ? [...questions] : [],
  );
  const [editModeId, setEditModeId] = useState<number | null>(null);
  const [scoreTypes, setScoreTypes] = useState<any[]>([]);

  useEffect(() => {
    if (questions) setAdminQuestions([...questions]);
  }, [questions]);

  useEffect(() => {
    fetch("/mock-score-types.json")
      .then((res) => res.json())
      .then(setScoreTypes)
      .catch(() => setScoreTypes([]));
  }, []);

  function handleMoveQuestionUp(questionId: number) {
    const idx = adminQuestions.findIndex((q) => q.id === questionId);
    if (idx <= 0) return;
    const newQuestions = [...adminQuestions];
    [newQuestions[idx - 1], newQuestions[idx]] = [
      newQuestions[idx],
      newQuestions[idx - 1],
    ];
    setAdminQuestions(newQuestions.map((q, i) => ({ ...q, position: i + 1 })));
  }

  function handleMoveQuestionDown(questionId: number) {
    const idx = adminQuestions.findIndex((q) => q.id === questionId);
    if (idx === -1 || idx === adminQuestions.length - 1) return;
    const newQuestions = [...adminQuestions];
    [newQuestions[idx], newQuestions[idx + 1]] = [
      newQuestions[idx + 1],
      newQuestions[idx],
    ];
    setAdminQuestions(newQuestions.map((q, i) => ({ ...q, position: i + 1 })));
  }

  function handleToggleActive(questionId: number) {
    const idx = adminQuestions.findIndex((q) => q.id === questionId);
    if (idx === -1) return;
    const newQuestions = [...adminQuestions];
    newQuestions[idx] = {
      ...newQuestions[idx],
      active: !newQuestions[idx].active,
    };
    setAdminQuestions(newQuestions);
  }

  return {
    adminQuestions,
    setAdminQuestions,
    editModeId,
    setEditModeId,
    scoreTypes,
    handleMoveQuestionUp,
    handleMoveQuestionDown,
    handleToggleActive,
  };
}
