import React, { useEffect } from "react";
import { Radio } from "@hh.ru/magritte-ui-checkbox-radio";
import "@hh.ru/magritte-ui-checkbox-radio/index.css";
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
  useEffect(() => {
    document.body.classList.add('magritte-old-layout');
    return () => {
      document.body.classList.remove('magritte-old-layout');
    };
  }, []);

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
          <div key={option.id} className={styles.optionWrapper}>
            <Radio
              name={`question-${question.id}`}
              checked={selectedOption === option.id}
              onChange={() => handleRadioChange(option.id)}
            />
            <span 
              className={styles.optionText} 
              onClick={() => handleRadioChange(option.id)}
              style={{ cursor: 'pointer' }}
            >
              {option.text}
            </span>
          </div>
        ))}
      </div>
      {error && <div className={styles.errorMessage}>{error}</div>}
    </div>
  );
};

export default SingleChoice;
