import React, { useEffect } from "react";
import { Radio } from "@hh.ru/magritte-ui-checkbox-radio";
import "@hh.ru/magritte-ui-checkbox-radio/index.css";
import styles from "./SingleChoice.module.css";

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
  selectedOption: number | null;
  onSelect: (id: number | null) => void;
  error: string | null;
  // admin mode
  isAdmin?: boolean;
  scoreTypes?: ScoreType[];
  onQuestionTextChange?: (newText: string) => void;
  onOptionChange?: (optIdx: number, field: string, value: any) => void;
  onOptionAdd?: () => void;
  onOptionRemove?: (optIdx: number) => void;
  onQuestionTypeChange?: (
    questionId: number,
    newType: "single-choice" | "multiple-choice",
  ) => void;
}

const SingleChoice: React.FC<Props> = ({
  question,
  selectedOption,
  onSelect,
  error,
  isAdmin = false,
  scoreTypes = [],
  onQuestionTextChange,
  onOptionChange,
  onOptionAdd,
  onOptionRemove,
  onQuestionTypeChange,
}) => {
  useEffect(() => {
    document.body.classList.add("magritte-old-layout");
    return () => {
      document.body.classList.remove("magritte-old-layout");
    };
  }, []);

  const handleRadioChange = (optionId: number) => {
    if (selectedOption === optionId) {
      onSelect(null);
    } else {
      onSelect(optionId);
    }
  };

  if (isAdmin) {
    return (
      <div className={styles.questionContainer}>
        {onQuestionTextChange && (
          <input
            type="text"
            value={question.questionText}
            onChange={(e) =>
              onQuestionTextChange && onQuestionTextChange(e.target.value)
            }
            style={{
              fontWeight: "bold",
              fontSize: 18,
              width: "100%",
              marginBottom: 8,
            }}
          />
        )}
        <div style={{ marginBottom: 16 }}>
          <span style={{ marginRight: 8 }}>Тип вопроса:</span>
          <label style={{ marginRight: 8 }}>
            <input
              type="radio"
              name={`question-type-${question.id}`}
              value="single-choice"
              checked={true}
              onChange={() =>
                onQuestionTypeChange?.(question.id, "single-choice")
              }
            />
            Одиночный выбор
          </label>
          <label>
            <input
              type="radio"
              name={`question-type-${question.id}`}
              value="multiple-choice"
              checked={false}
              onChange={() =>
                onQuestionTypeChange?.(question.id, "multiple-choice")
              }
            />
            Множественный выбор
          </label>
        </div>
        <div style={{ marginLeft: 16 }}>
          {question.options?.map((opt, optIdx) => (
            <div
              key={opt.id}
              style={{
                display: "flex",
                alignItems: "center",
                marginBottom: 4,
                gap: 8,
              }}
            >
              <input
                type="text"
                value={opt.text}
                onChange={(e) =>
                  onOptionChange &&
                  onOptionChange(optIdx, "text", e.target.value)
                }
                style={{ width: 320 }}
              />
              <select
                value={opt.scores && opt.scores[0] ? opt.scores[0].code : ""}
                onChange={(e) =>
                  onOptionChange &&
                  onOptionChange(optIdx, "scoreType", e.target.value)
                }
                style={{ width: 180 }}
              >
                <option value="" disabled>
                  Выберите оценку
                </option>
                {scoreTypes.map((st) => (
                  <option key={st.code} value={st.code}>
                    {st.title}
                  </option>
                ))}
              </select>
              <input
                type="number"
                value={
                  opt.scores &&
                  opt.scores[0] &&
                  opt.scores[0].weight !== undefined
                    ? String(opt.scores[0].weight)
                    : ""
                }
                onChange={(e) =>
                  onOptionChange &&
                  onOptionChange(optIdx, "weight", e.target.value)
                }
                style={{ width: 60 }}
              />
              <button
                onClick={() => onOptionRemove && onOptionRemove(optIdx)}
                style={{
                  color: "red",
                  border: "none",
                  background: "none",
                  cursor: "pointer",
                }}
                title="Удалить вариант"
              >
                ×
              </button>
            </div>
          ))}
          <button
            onClick={() => onOptionAdd && onOptionAdd()}
            style={{
              marginTop: 4,
              color: "#1976d2",
              border: "none",
              background: "none",
              cursor: "pointer",
            }}
          >
            + Добавить вариант
          </button>
        </div>
      </div>
    );
  }

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
              style={{ cursor: "pointer" }}
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
