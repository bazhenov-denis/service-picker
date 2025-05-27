import React, { useState } from "react";
import { Input } from "@hh.ru/magritte-ui-input";
import styles from "./VacanciesNumber.module.css";
import type { InputQuestion } from "../../types/question";

interface Props {
  question: InputQuestion;
  onNumberChange: (value: number) => void;
  error: string | null;
  value?: number;
}

const VacanciesNumber: React.FC<Props> = ({
  question,
  onNumberChange,
  error,
  value,
}) => {
  const [localError, setLocalError] = useState<string | null>(null);
  const min = question.validation?.min ?? 1;
  const max = question.validation?.max ?? 100;

  const validateValue = (newValue: string): string | null => {
    if (newValue === "") {
      return null;
    }

    const numValue = Number(newValue);
    
    if (isNaN(numValue) || !Number.isInteger(numValue)) {
      return `Введите число от ${min} до ${max}`;
    }

    if (numValue < min) {
      return `Минимальное значение: ${min}`;
    }

    if (numValue > max) {
      return `Максимальное значение: ${max}`;
    }

    return null;
  };

  const handleChange = (newValue: string) => {
    const validationError = validateValue(newValue);
    setLocalError(validationError);

    if (validationError) {
      return;
    }

    if (newValue === "") {
      onNumberChange(0);
      return;
    }

    const numValue = Number(newValue);
    onNumberChange(numValue);
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.questionTitle}>{question.questionText}</h2>
      <Input
        value={value === 0 ? "" : value?.toString() ?? ""}
        onChange={handleChange}
        placeholder={question.placeholder}
        invalid={!!(error || localError)}
        errorMessage={error || localError}
        type="text"
        inputMode="numeric"
        pattern="[0-9]*"
        size="medium"
      />
    </div>
  );
};

export default VacanciesNumber;
