import React from "react";
import styles from "./MultipleChoice.module.css";

interface Option {
  id: number;
  text: string;
}

interface Props {
  question: {
    id: number;
    questionText: string;
    options?: Option[];
  };
  selectedOptions: number[];
  onToggle: (ids: number[]) => void;
  error: string | null;
}

const MultipleChoice: React.FC<Props> = ({
  question,
  selectedOptions,
  onToggle,
  error,
}) => {
  const handleToggle = (optionId: number) => {
    const newSelected = selectedOptions.includes(optionId)
      ? selectedOptions.filter((id) => id !== optionId)
      : [...selectedOptions, optionId];
    onToggle(newSelected);
  };

  return (
    <div className={styles.multipleChoiceContainer}>
      <h2 className={styles.questionTitle}>{question.questionText}</h2>
      <div className={styles.optionsContainer}>
        {question.options?.map((option) => (
          <label key={option.id} className={styles.optionLabel}>
            <input
              type="checkbox"
              id={`option-${option.id}`}
              checked={selectedOptions.includes(option.id)}
              onChange={() => handleToggle(option.id)}
              className={styles.checkbox}
            />
            <span className={styles.optionText}>{option.text}</span>
          </label>
        ))}
      </div>
      {error && <div className={styles.errorMessage}>{error}</div>}
    </div>
  );
};

export default MultipleChoice;
