import React from "react";
import styles from "./MultipleChoice.module.css";

interface Option {
  id: number;
  text: string;
}

interface OptionWithScores {
  id: number;
  text: string;
  scores: { id: number; code: string; title: string; weight: number }[];
}

interface ScoreType {
  id: number;
  code: string;
  title: string;
}

interface Props {
  question: {
    id: number;
    questionText: string;
    options?: OptionWithScores[];
  };
  selectedOptions: number[];
  onToggle: (ids: number[]) => void;
  error: string | null;
  // admin mode
  isAdmin?: boolean;
  scoreTypes?: ScoreType[];
  onQuestionTextChange?: (newText: string) => void;
  onOptionChange?: (optIdx: number, field: string, value: any) => void;
  onOptionAdd?: () => void;
  onOptionRemove?: (optIdx: number) => void;
}

const MultipleChoice: React.FC<Props> = ({
  question,
  selectedOptions,
  onToggle,
  error,
  isAdmin = false,
  scoreTypes = [],
  onQuestionTextChange,
  onOptionChange,
  onOptionAdd,
  onOptionRemove,
}) => {
  const handleToggle = (optionId: number) => {
    const newSelected = selectedOptions.includes(optionId)
      ? selectedOptions.filter((id) => id !== optionId)
      : [...selectedOptions, optionId];
    onToggle(newSelected);
  };

  if (isAdmin) {
    return (
      <div className={styles.multipleChoiceContainer}>
        <input
          type="text"
          value={question.questionText}
          onChange={e => onQuestionTextChange && onQuestionTextChange(e.target.value)}
          style={{ fontWeight: 'bold', fontSize: 18, width: '100%', marginBottom: 8 }}
        />
        <div style={{ marginLeft: 16 }}>
          {question.options?.map((opt, optIdx) => (
            <div key={opt.id} style={{ display: 'flex', alignItems: 'center', marginBottom: 4, gap: 8 }}>
              <input
                type="text"
                value={opt.text}
                onChange={e => onOptionChange && onOptionChange(optIdx, 'text', e.target.value)}
                style={{ width: 320 }}
              />
              <select
                value={opt.scores && opt.scores[0] ? opt.scores[0].code : ''}
                onChange={e => onOptionChange && onOptionChange(optIdx, 'scoreType', e.target.value)}
                style={{ width: 180 }}
              >
                <option value="" disabled>Выберите оценку</option>
                {scoreTypes.map(st => (
                  <option key={st.code} value={st.code}>{st.title}</option>
                ))}
              </select>
              <input
                type="number"
                value={opt.scores && opt.scores[0] && opt.scores[0].weight !== undefined ? String(opt.scores[0].weight) : ''}
                onChange={e => onOptionChange && onOptionChange(optIdx, 'weight', e.target.value)}
                style={{ width: 60 }}
              />
              <button
                onClick={() => onOptionRemove && onOptionRemove(optIdx)}
                style={{ color: 'red', border: 'none', background: 'none', cursor: 'pointer' }}
                title="Удалить вариант"
              >
                ×
              </button>
            </div>
          ))}
          <button
            onClick={() => onOptionAdd && onOptionAdd()}
            style={{ marginTop: 4, color: '#1976d2', border: 'none', background: 'none', cursor: 'pointer' }}
          >
            + Добавить вариант
          </button>
        </div>
      </div>
    );
  }

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
