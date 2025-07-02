import React, { useState, useEffect } from "react";
import styles from "./AddQuestionButton.module.css";
import QuestionEditModal from "./QuestionEditModal";
import type {
  Question,
  SingleChoiceQuestion,
  MultipleChoiceQuestion,
  OptionWithScores,
  OptionScore,
} from "../../types/question";

interface ScoreType {
  id: number;
  name: string;
}

interface Option {
  text: string;
  score: { code: string; weight: number };
}

const AddQuestionButton: React.FC = () => {
  const [open, setOpen] = useState(false);
  const [scoreTypes, setScoreTypes] = useState<ScoreType[]>([]);
  const [questionText, setQuestionText] = useState("");
  const [shortTitle, setShortTitle] = useState("");
  const [type, setType] = useState("");
  const [referenceType, setReferenceType] = useState("");
  const [isRequired, setIsRequired] = useState(true);
  const [active, setActive] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [draftQuestion, setDraftQuestion] = useState<Question | null>(null);

  useEffect(() => {
    fetch("http://localhost:8080/score-types")
      .then((res) => res.json())
      .then((data) => setScoreTypes(data))
      .catch(() => setScoreTypes([]));
  }, []);

  const handleOpen = () => {
    setDraftQuestion({
      id: Date.now(),
      questionText: "",
      shortTitle: "",
      isRequired: false,
      type: "single-choice",
      active: true,
      position: 0,
      options: [
        {
          id: Date.now(),
          text: "",
          scores: [{ id: Date.now(), code: "", title: "", weight: 1 }],
        },
      ],
    } as SingleChoiceQuestion);
    setOpen(true);
  };

  const handleSave = async () => {
    if (!draftQuestion) return;
    setLoading(true);
    setError("");
    setSuccess(false);
    try {
      let optionsArr = (draftQuestion as any).options || [];
      let payload: any = {
        questionText: draftQuestion.questionText,
        type: draftQuestion.type,
        isRequired: draftQuestion.isRequired,
        shortTitle: draftQuestion.shortTitle,
        active: draftQuestion.active,
        options: optionsArr.map((opt: any) => ({
          text: opt.text,
          score:
            opt.scores && opt.scores[0]
              ? { code: opt.scores[0].code, weight: opt.scores[0].weight }
              : { code: "", weight: 1 },
        })),
      };
      if (draftQuestion.type === "reference") {
        payload.referenceType = (draftQuestion as any).referenceType || "";
      }
      const response = await fetch("http://localhost:8080/questions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!response.ok) throw new Error("Ошибка при создании вопроса");
      setSuccess(true);
      setOpen(false);
      setDraftQuestion(null);
    } catch (e: any) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  // Преобразуем scoreTypes к нужному формату
  const mappedScoreTypes = scoreTypes.map((st: any) => ({
    id: st.id,
    code: st.code || st.name || "",
    title: st.title || st.name || "",
  }));

  return (
    <>
      <button className={styles.fab} onClick={handleOpen}>
        +
      </button>
      {open && draftQuestion && (
        <QuestionEditModal
          question={draftQuestion}
          scoreTypes={mappedScoreTypes}
          onChange={setDraftQuestion}
          onSave={handleSave}
          onCancel={() => {
            setOpen(false);
            setDraftQuestion(null);
          }}
          isSaving={loading}
          error={error}
        />
      )}
    </>
  );
};

export default AddQuestionButton;
