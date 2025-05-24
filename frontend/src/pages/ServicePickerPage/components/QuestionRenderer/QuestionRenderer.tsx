import React from "react";
import HierarchicalSelector from "../HierarchicalSelector/HierarchicalSelector";
import VacanciesNumber from "../VacanciesNumber/VacanciesNumber";
import SingleChoice from "../SingleChoice/SingleChoice";
import MultipleChoice from "../MultipleChoice/MultipleChoice";
import type {
  Question,
  ReferenceQuestion,
  InputQuestion,
  SingleChoiceQuestion,
  MultipleChoiceQuestion,
} from "../../types/question";

import styles from "./QuestionRenderer.module.css";

interface Props {
  question: Question & { type: string };
  answer: any;
  onChange: (value: any) => void;
  error: string | null;
  collection?: any;
}

const QuestionRenderer: React.FC<Props> = ({
  question,
  answer,
  onChange,
  error,
  collection,
}) => {
  const handleChange = (value: unknown) => {
    onChange({ type: question.type, value });
  };

  if (question.type === "reference") {
    return (
      <HierarchicalSelector
        title={question.questionText}
        collection={collection || []}
        selectedItems={(answer as { value: string[] })?.value || []}
        onItemsChange={(items) => handleChange(items)}
        loading={!collection}
        error={error}
        dataQa={`${(question as ReferenceQuestion).referenceType}-selector`}
      />
    );
  }

  if (question.type === "input") {
    return (
      <VacanciesNumber
        onNumberChange={(value) => handleChange(value)}
        error={error}
        question={question as InputQuestion}
      />
    );
  }

  if (question.type === "single-choice") {
    return (
      <SingleChoice
        question={question as SingleChoiceQuestion}
        selectedOption={(answer as { value: number | null })?.value}
        onSelect={(id) => handleChange(id)}
        error={error}
      />
    );
  }

  if (question.type === "multiple-choice") {
    return (
      <MultipleChoice
        question={question as MultipleChoiceQuestion}
        selectedOptions={(answer as { value: number[] })?.value || []}
        onToggle={(ids) => handleChange(ids)}
        error={error}
      />
    );
  }

  const unknownType = (question as { type: string }).type;
  console.error(`Неизвестный тип вопроса: ${unknownType}`, question);
  return (
    <div className={styles.unknownQuestionType}>
      ❌ Неизвестный тип вопроса: <strong>{unknownType}</strong>
      <p>Пожалуйста, проверьте данные на сервере.</p>
    </div>
  );
};

export default QuestionRenderer;
