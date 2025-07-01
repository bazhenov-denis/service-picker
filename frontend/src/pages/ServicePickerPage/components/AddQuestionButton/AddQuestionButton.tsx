import React, { useState, useEffect } from 'react';
import styles from './AddQuestionButton.module.css';
import QuestionEditModal from "./QuestionEditModal";
import type { Question, SingleChoiceQuestion, MultipleChoiceQuestion } from "../../types/question";

interface ScoreType {
  id: number;
  name: string;
}

interface Option {
  text: string;
  scores: number[];
}

const AddQuestionButton: React.FC = () => {
  const [open, setOpen] = useState(false);
  const [scoreTypes, setScoreTypes] = useState<ScoreType[]>([]);
  const [questionText, setQuestionText] = useState('');
  const [shortTitle, setShortTitle] = useState('');
  const [type, setType] = useState('');
  const [referenceType, setReferenceType] = useState('');
  const [isRequired, setIsRequired] = useState(true);
  const [active, setActive] = useState(true);
  const [options, setOptions] = useState<Option[]>([{ text: '', scores: [] }]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [draftQuestion, setDraftQuestion] = useState<Question | null>(null);

  useEffect(() => {
    fetch('http://localhost:8080/score-types')
      .then(res => res.json())
      .then(data => setScoreTypes(data))
      .catch(() => setScoreTypes([]));
  }, []);

  const handleOptionChange = (idx: number, field: 'text' | 'scores', value: string | number[]) => {
    setOptions(prev => prev.map((opt, i) =>
      i === idx ? { ...opt, [field]: value } : opt
    ));
  };

  const addOption = () => setOptions([...options, { text: '', scores: [] }]);
  const removeOption = (idx: number) => setOptions(options.filter((_, i) => i !== idx));

  const handleOpen = () => {
    setDraftQuestion({
      id: Date.now(),
      questionText: "",
      isRequired: true,
      type: "single-choice",
      active: true,
      position: 0,
      options: [],
    } as SingleChoiceQuestion);
    setOpen(true);
  };

  const handleSave = async () => {
    if (!draftQuestion) return;
    setLoading(true);
    setError("");
    setSuccess(false);
    try {
      const response = await fetch("http://localhost:8080/questions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(draftQuestion),
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
    code: st.code || st.name || '',
    title: st.title || st.name || '',
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
          onCancel={() => { setOpen(false); setDraftQuestion(null); }}
          isSaving={loading}
          error={error}
        />
      )}
    </>
  );
};

export default AddQuestionButton; 