import { useState, useEffect } from "react";
import { useQuestions } from "./useQuestions";
import type { Question } from "../types/question";
import axios from "axios";

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
    axios
      .get("http://localhost:8080/score-types")
      .then((res) => setScoreTypes(res.data))
      .catch(() => setScoreTypes([]));
  }, []);

  function handleMoveQuestionUp(questionId: number) {
    const sortedQuestions = [...adminQuestions].sort(
      (a, b) => (a.position ?? 0) - (b.position ?? 0),
    );
    const idx = sortedQuestions.findIndex((q) => q.id === questionId);
    if (idx <= 0) return;
    [sortedQuestions[idx - 1], sortedQuestions[idx]] = [
      sortedQuestions[idx],
      sortedQuestions[idx - 1],
    ];
    const posA = sortedQuestions[idx].position;
    const posB = sortedQuestions[idx - 1].position;
    sortedQuestions[idx].position = posB;
    sortedQuestions[idx - 1].position = posA;
    const updated = adminQuestions.map((q) => {
      const updatedQ = sortedQuestions.find((sq) => sq.id === q.id);
      return updatedQ ? { ...updatedQ } : q;
    });
    setAdminQuestions(updated);
  }

  function handleMoveQuestionDown(questionId: number) {
    const sortedQuestions = [...adminQuestions].sort(
      (a, b) => (a.position ?? 0) - (b.position ?? 0),
    );
    const idx = sortedQuestions.findIndex((q) => q.id === questionId);
    if (idx === -1 || idx === sortedQuestions.length - 1) return;
    [sortedQuestions[idx], sortedQuestions[idx + 1]] = [
      sortedQuestions[idx + 1],
      sortedQuestions[idx],
    ];
    const posA = sortedQuestions[idx].position;
    const posB = sortedQuestions[idx + 1].position;
    sortedQuestions[idx].position = posB;
    sortedQuestions[idx + 1].position = posA;
    const updated = adminQuestions.map((q) => {
      const updatedQ = sortedQuestions.find((sq) => sq.id === q.id);
      return updatedQ ? { ...updatedQ } : q;
    });
    setAdminQuestions(updated);
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

  function handleQuestionTypeChange(
    questionId: number,
    newType: "single-choice" | "multiple-choice",
  ) {
    setAdminQuestions((prevQuestions) =>
      prevQuestions.map((q) => {
        if (q.id === questionId) {
          if (q.type === "single-choice" || q.type === "multiple-choice") {
            // @ts-ignore
            return { ...q, type: newType };
          }
        }
        return q;
      }),
    );
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
    handleQuestionTypeChange,
  };
}
