import React from "react";
import { Card } from "@hh.ru/magritte-ui-card";
import { Text } from "@hh.ru/magritte-ui-typography";
import styles from "./SelectedState.module.css";
import type { Question } from "../../types/question";

interface SelectedStateProps {
  questions: Question[];
  answers: Record<number, any>;
  getDisplayValue: (questionId: number, value: any) => string[];
  onQuestionClick?: (questionId: number) => void;
  onSegmentChange?: (segment: number) => void;
  currentSegment?: number;
  questionsPerSegment?: number;
}

export const SelectedState: React.FC<SelectedStateProps> = ({
  questions,
  answers,
  getDisplayValue,
  onQuestionClick,
  onSegmentChange,
  currentSegment = 0,
  questionsPerSegment = 3,
}) => {
  const handleQuestionClick = (questionId: number) => {
    if (onSegmentChange) {
      const questionIndex = questions.findIndex(q => q.id === questionId);
      if (questionIndex !== -1) {
        const targetSegment = Math.floor(questionIndex / questionsPerSegment);
        if (targetSegment !== currentSegment) {
          onSegmentChange(targetSegment);
        }
      }
    }
    
    if (onQuestionClick) {
      onQuestionClick(questionId);
    }
  };

  const renderAnswer = (question: Question) => {
    const value = answers[question.id];
    if (value === undefined || value === null) return null;

    const displayValues = getDisplayValue(question.id, value);
    if (!displayValues?.length) return null;

    return (
      <div 
        className={styles.parameter} 
        key={question.id}
        onClick={() => handleQuestionClick(question.id)}
      >
        <Text typography="subtitle-1-semibold" className={styles.question}>
          {question.questionText}
        </Text>
        <Text typography="label-3-regular" className={styles.answer}>
          {displayValues.join(", ")}
        </Text>
      </div>
    );
  };

  const hasAnswers = questions.some((question) => {
    const value = answers[question.id];
    return (
      value !== undefined &&
      value !== null &&
      (Array.isArray(value) ? value.length > 0 : true)
    );
  });

  return (
    <div className={styles.container}>
      <Card
        className={styles.card}
        padding={16}
        borderRadius={8}
        style="primary"
        shadow="level-2"
      >
        <h3 className={styles.title}>Выбранные параметры</h3>
        <div className={styles.content}>
          {hasAnswers ? (
            questions.map((question) => renderAnswer(question))
          ) : (
            <Text typography="label-3-regular" className={styles.emptyState}>
              Параметры не выбраны
            </Text>
          )}
        </div>
      </Card>
    </div>
  );
};
