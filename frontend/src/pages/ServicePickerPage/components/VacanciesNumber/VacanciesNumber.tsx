import React from "react";
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
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = parseInt(e.target.value, 10);
    if (!isNaN(newValue)) {
      onNumberChange(newValue);
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.questionTitle}>{question.questionText}</h2>
      <input
        type="number"
        min={question.validation?.min ?? 1}
        max={question.validation?.max ?? 100}
        value={value ?? ""}
        onChange={handleChange}
        className={styles.input}
        placeholder={question.placeholder}
      />
      {error && <div className={styles.errorMessage}>{error}</div>}
    </div>
  );
};

export default VacanciesNumber;
