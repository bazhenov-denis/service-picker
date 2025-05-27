import { FC } from "react";
import { Button } from "@hh.ru/magritte-ui-button";
import "@hh.ru/magritte-ui-button/index.css";
import styles from "./ServicePickerButton.module.css";
import type { Question } from "../../types/question";

type AnswerValue = string[] | number[] | number;

interface ServicePickerButtonProps {
  answers: Record<string, AnswerValue>;
  questions: Question[];
  onSubmit: () => void;
  isLoading?: boolean;
  error?: string | null;
}

export const ServicePickerButton: FC<ServicePickerButtonProps> = ({
  answers,
  questions,
  onSubmit,
  isLoading = false,
  error = null,
}) => {
  const isDisabled =
    questions.some((question) => {
      if (!question.isRequired) return false;
      const answer = answers[question.id.toString()];
      return !answer || (Array.isArray(answer) && answer.length === 0);
    }) || isLoading;

  return (
    <div className={styles.buttonContainer}>
      <Button
        mode="primary"
        size="large"
        style="accent"
        stretched
        disabled={isDisabled}
        onClick={onSubmit}
        data-qa="service-picker-button"
      >
        <span className={styles.buttonText}>
          {isLoading ? "Отправка..." : "Подобрать услугу"}
        </span>
      </Button>
      {error && <div className={styles.error}>{error}</div>}
    </div>
  );
};

export default ServicePickerButton;
