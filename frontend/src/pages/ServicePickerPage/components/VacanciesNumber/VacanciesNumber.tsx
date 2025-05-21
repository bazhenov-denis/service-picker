import React, { FC, useState } from "react";
import styles from "./VacanciesNumber.module.css";

interface Props {
  onNumberChange: (value: number) => void;
  error: string | null;
  question: {
    id: number;
    questionText: string;
    validation?: { min: number; max: number };
    placeholder?: string;
  };
  placeholder?: string;
}

const VacanciesNumber: FC<Props> = ({ onNumberChange, error, question }) => {
  const [value, setValue] = useState<string>("");
  const [inputError, setInputError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let inputValue = e.target.value;

    const sanitizedValue = inputValue.replace(/[^0-9]/g, "");

    if (sanitizedValue.length > 1 && sanitizedValue.startsWith("0")) {
      setInputError("Число не может начинаться с 0");
      return;
    }

    const { min = 1, max = 100 } = question.validation || {};
    if (sanitizedValue) {
      const numValue = parseInt(sanitizedValue, 10);
      if (numValue < min || numValue > max) {
        setInputError(`Введите число от ${min} до ${max}`);
        return;
      }
    }

    setValue(sanitizedValue);
    setInputError(null);
    onNumberChange(sanitizedValue ? parseInt(sanitizedValue, 10) : 0);
  };

  return (
    <div className={styles.vacanciesNumber}>
      <h2 className={styles.title}>{question.questionText}</h2>
      <input
        type="text"
        value={value}
        onChange={handleChange}
        placeholder={question.placeholder || "Введите число"}
        className={`${styles.input} ${inputError ? styles.inputError : ""}`}
        inputMode="numeric"
        pattern="[0-9]*"
      />
      {inputError && <div className={styles.errorMessage}>{inputError}</div>}
      {error && <div className={styles.errorMessage}>{error}</div>}
    </div>
  );
};

export default VacanciesNumber;
