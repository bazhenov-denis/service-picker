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
  AnswerValue,
  QuestionBase,
} from "../../types/question";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";

import styles from "./QuestionRenderer.module.css";

interface Props {
  question: Question;
  answer: AnswerValue;
  onChange: (value: any) => void;
  error: string | null;
  collection?: TreeCollection;
  getDisplayValue?: (questionId: number, value: AnswerValue) => string[];
}

const isStringArray = (arr: unknown): arr is string[] => {
  return (
    Array.isArray(arr) &&
    arr.every((item): item is string => typeof item === "string")
  );
};

const isNumberArray = (arr: unknown): arr is number[] => {
  return (
    Array.isArray(arr) &&
    arr.every((item): item is number => typeof item === "number")
  );
};

const QuestionRenderer: React.FC<Props> = ({
  question,
  answer,
  onChange,
  error,
  collection,
  getDisplayValue,
}) => {
  const handleChange = (value: unknown) => {
    onChange(value);
  };

  if (question.type === "reference") {
    if (!collection || !(collection instanceof TreeCollection)) {
      return (
        <div className={styles.errorContainer}>
          <div>
            <p>Ошибка при загрузке данных</p>
            <p className={styles.errorMessage}>
              Не удалось загрузить данные для выбора
            </p>
          </div>
        </div>
      );
    }

    const displayValue = getDisplayValue
      ? getDisplayValue(question.id, answer)
      : isStringArray(answer)
        ? answer
        : [];

    return (
      <HierarchicalSelector
        title={question.questionText}
        collection={collection}
        selectedItems={displayValue}
        onItemsChange={(items) => handleChange(items)}
        loading={!collection}
        error={error}
        dataQa={`${(question as ReferenceQuestion).referenceType}-selector`}
      />
    );
  }

  if (question.type === "input") {
    const value = Array.isArray(answer) ? answer[0] : answer;
    return (
      <VacanciesNumber
        onNumberChange={(value) => handleChange(value)}
        error={error}
        question={question as InputQuestion}
        value={typeof value === "number" ? value : undefined}
      />
    );
  }

  if (question.type === "single-choice") {
    const value = Array.isArray(answer) ? answer[0] : answer;
    return (
      <SingleChoice
        question={question as SingleChoiceQuestion}
        selectedOption={typeof value === "number" ? value : null}
        onSelect={(id) => handleChange(id)}
        error={error}
      />
    );
  }

  if (question.type === "multiple-choice") {
    return (
      <MultipleChoice
        question={question as MultipleChoiceQuestion}
        selectedOptions={isNumberArray(answer) ? answer : []}
        onToggle={(ids) => handleChange(ids)}
        error={error}
      />
    );
  }

  console.error(
    `Неизвестный тип вопроса: ${(question as QuestionBase).type}`,
    question,
  );
  return (
    <div className={styles.unknownQuestionType}>
      ❌ Неизвестный тип вопроса:{" "}
      <strong>{(question as QuestionBase).type}</strong>
      <p>Пожалуйста, проверьте данные на сервере.</p>
    </div>
  );
};

export default QuestionRenderer;
