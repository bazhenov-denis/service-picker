import React from "react";
import QuestionRenderer from "../QuestionRenderer/QuestionRenderer";
import styles from "./AddQuestionButton.module.css";

import type { Question, SingleChoiceQuestion, MultipleChoiceQuestion } from "../../types/question";

interface QuestionEditModalProps {
  question: Question;
  scoreTypes: { id: number; code: string; title: string }[];
  onChange: (updated: Question) => void;
  onSave: () => void;
  onCancel: () => void;
  isSaving: boolean;
  error?: string;
}

const QuestionEditModal: React.FC<QuestionEditModalProps> = ({
  question,
  scoreTypes,
  onChange,
  onSave,
  onCancel,
  isSaving,
  error,
}) => {
  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modal}>
        <h2>{question.id ? "Редактировать вопрос" : "Создать новый вопрос"}</h2>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
          <label>
            <span style={{ fontWeight: 500 }}>Название вопроса</span>
            <input
              type="text"
              value={question.questionText}
              onChange={e => onChange({ ...question, questionText: e.target.value })}
              style={{ width: '100%', marginTop: 4 }}
            />
          </label>
          <label>
            <span style={{ fontWeight: 500 }}>Короткий заголовок (ShortTitle)</span>
            <input
              type="text"
              value={question.shortTitle || ''}
              onChange={e => onChange({ ...question, shortTitle: e.target.value })}
              style={{ width: '100%', marginTop: 4 }}
            />
          </label>
          <label style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <input
              type="checkbox"
              checked={question.isRequired}
              onChange={e => onChange({ ...question, isRequired: e.target.checked })}
            />
            <span>Обязательный вопрос</span>
          </label>
        </div>
        <QuestionRenderer
          question={question}
          answer={undefined}
          onChange={() => {}}
          error={error || null}
          isAdmin={true}
          scoreTypes={scoreTypes}
          onQuestionTextChange={undefined}
          onOptionChange={(optIdx, field, value) => {
            if (
              question.type !== "single-choice" &&
              question.type !== "multiple-choice"
            )
              return;
            const newOptions = [...(question.options || [])];
            if (field === "text") {
              newOptions[optIdx] = {
                ...newOptions[optIdx],
                text: value,
              };
            } else if (field === "scoreType") {
              // обработка выбора типа оценки
              const scores = newOptions[optIdx].scores ? [...newOptions[optIdx].scores] : [];
              const selectedScoreType = scoreTypes.find(st => st.code === value);
              if (scores[0]) {
                scores[0] = {
                  ...scores[0],
                  id: selectedScoreType?.id || 0,
                  code: selectedScoreType?.code || value,
                  title: selectedScoreType?.title || '',
                };
              } else {
                scores[0] = {
                  id: selectedScoreType?.id || 0,
                  code: selectedScoreType?.code || value,
                  title: selectedScoreType?.title || '',
                  weight: 0,
                };
              }
              newOptions[optIdx] = { ...newOptions[optIdx], scores };
            } else if (field === "weight") {
              // обработка веса если нужно
              const scores = newOptions[optIdx].scores ? [...newOptions[optIdx].scores] : [];
              if (scores[0]) {
                scores[0] = { ...scores[0], weight: Number(value) };
              } else {
                // если scores ещё нет, создаём новый объект оценки
                scores[0] = { id: Date.now(), code: "", title: "", weight: Number(value) };
              }
              newOptions[optIdx] = { ...newOptions[optIdx], scores };
            }
            onChange({ ...question, options: newOptions } as SingleChoiceQuestion | MultipleChoiceQuestion);
          }}
          onOptionAdd={() => {
            if (
              question.type !== "single-choice" &&
              question.type !== "multiple-choice"
            )
              return;
            const newOptions = [
              ...(question.options || []),
              { id: Date.now(), text: "", scores: [] },
            ];
            onChange({ ...question, options: newOptions } as SingleChoiceQuestion | MultipleChoiceQuestion);
          }}
          onOptionRemove={(optIdx) => {
            if (
              question.type !== "single-choice" &&
              question.type !== "multiple-choice"
            )
              return;
            const newOptions = question.options.filter((_, i) => i !== optIdx);
            onChange({ ...question, options: newOptions } as SingleChoiceQuestion | MultipleChoiceQuestion);
          }}
          onQuestionTypeChange={(id, newType) => {
            if (newType === "single-choice" || newType === "multiple-choice") {
              const options =
                (question.type === "single-choice" || question.type === "multiple-choice") && question.options
                  ? question.options
                  : [];
              onChange({
                ...question,
                type: newType,
                options,
              } as SingleChoiceQuestion | MultipleChoiceQuestion);
            } else {
              const q: Question = { ...question, type: newType };
              // @ts-ignore: options не нужен для других типов
              delete (q as any).options;
              onChange(q);
            }
          }}
        />
        <div style={{ display: "flex", gap: 12, marginTop: 24 }}>
          <button onClick={onSave} disabled={isSaving}>
            {isSaving ? "Сохраняем..." : "Готово"}
          </button>
          <button onClick={onCancel} disabled={isSaving}>
            Отмена
          </button>
        </div>
        {error && <div className={styles.error}>{error}</div>}
      </div>
    </div>
  );
};

export default QuestionEditModal; 