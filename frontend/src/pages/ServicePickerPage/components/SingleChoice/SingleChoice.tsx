import React from "react";
import styles from "./SingleChoice.module.css";

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
  selectedOption: number | null;
  onSelect: (id: number | null) => void;
  error: string | null;
}

const SingleChoice: React.FC<Props> = ({
  question,
  selectedOption,
  onSelect,
  error,
}) => {
  const handleRadioChange = (optionId: number) => {
    if (selectedOption === optionId) {
      onSelect(null);
    } else {
      onSelect(optionId);
    }
  };

  return (
    <div className={styles.questionContainer}>
      <h2 className={styles.questionTitle}>{question.questionText}</h2>
      <div className={styles.optionsContainer}>
        {question.options?.map((option) => (
          <label key={option.id} className={styles.optionLabel}>
            <input
              type="radio"
              name={`question-${question.id}`}
              checked={selectedOption === option.id}
              onChange={() => handleRadioChange(option.id)}
              className={styles.radioInput}
            />
            <span className={styles.radioCustom}></span>
            <span className={styles.optionText}>{option.text}</span>
          </label>
        ))}
      </div>
      {error && <div className={styles.errorMessage}>{error}</div>}
    </div>
  );
};

export default SingleChoice;
