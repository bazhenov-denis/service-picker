import { useState, useMemo } from "react";
import type { Question } from "../types/question";
import type { Answers } from "./useServicePicker";

const QUESTIONS_PER_SEGMENT = 3;

export function useQuestionSegments(questions: Question[], answers: Answers) {
  const [currentSegment, setCurrentSegment] = useState(0);

  const totalSegments = useMemo(() => {
    return Math.ceil((questions?.length || 0) / QUESTIONS_PER_SEGMENT);
  }, [questions]);

  const currentSegmentQuestions = useMemo(() => {
    if (!questions) return [];
    const sortedQuestions = [...questions].sort((a, b) => (a.position ?? 0) - (b.position ?? 0));
    const startIndex = currentSegment * QUESTIONS_PER_SEGMENT;
    const endIndex = startIndex + QUESTIONS_PER_SEGMENT;
    return sortedQuestions.slice(startIndex, endIndex);
  }, [questions, currentSegment]);

  const canProceedToNext = useMemo(() => {
    return currentSegmentQuestions.every((question) => {
      if (!question.isRequired) return true;
      const answer = answers[question.id];
      return answer && (Array.isArray(answer) ? answer.length > 0 : true);
    });
  }, [currentSegmentQuestions, answers]);

  const isLastSegment = currentSegment === totalSegments - 1;

  const handleSegmentChange = (newSegment: number) => {
    setCurrentSegment(newSegment);
  };

  return {
    currentSegment,
    totalSegments,
    currentSegmentQuestions,
    canProceedToNext,
    isLastSegment,
    handleSegmentChange,
    QUESTIONS_PER_SEGMENT,
  };
}
