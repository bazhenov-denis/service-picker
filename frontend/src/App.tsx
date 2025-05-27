import React from "react";
import Header from "./pages/ServicePickerPage/components/Header/Header";
import QuestionRenderer from "./pages/ServicePickerPage/components/QuestionRenderer/QuestionRenderer";
import ServicePickerButton from "./pages/ServicePickerPage/components/ServicePickerButton/ServicePickerButton";
import OfferDisplay from "./pages/ServicePickerPage/components/OfferDisplay/OfferDisplay";
import { SelectedState } from "./pages/ServicePickerPage/components/SelectedState/SelectedState";
import { useServicePicker } from "./pages/ServicePickerPage/hooks/useServicePicker";
import { useRegions } from "./pages/ServicePickerPage/hooks/useRegions";
import { useProfessions } from "./pages/ServicePickerPage/hooks/useProfessions";
import { useQuestions } from "./pages/ServicePickerPage/hooks/useQuestions";
import styles from "./App.module.css";
import type { Question } from "./pages/ServicePickerPage/types/question";
import type { ReferenceQuestion } from "./pages/ServicePickerPage/types/question";

const App: React.FC = () => {
  const {
    questions,
    isLoading: questionsLoading,
    error: questionsError,
  } = useQuestions();
  const { collection: regionsCollection } = useRegions();
  const { collection: professionsCollection } = useProfessions();
  const {
    answers,
    setAnswer,
    validationErrors,
    offer,
    error,
    isLoading,
    handleSubmit,
    getDisplayValue,
  } = useServicePicker(questions || []);

  if (questionsLoading) {
    return <div>Загрузка вопросов...</div>;
  }

  if (questionsError) {
    return <div>Ошибка: {questionsError}</div>;
  }

  return (
    <div className={styles.app}>
      <Header />
      <div className={styles.container}>
        <div className={styles.mainContent}>
          <div className={styles.questionsContainer}>
            {questions?.map((question) => {
              const collection =
                question.type === "reference" &&
                (question as ReferenceQuestion).referenceType === "regions"
                  ? regionsCollection
                  : question.type === "reference" &&
                      (question as ReferenceQuestion).referenceType ===
                        "professions"
                    ? professionsCollection
                    : undefined;

              return (
                <QuestionRenderer
                  key={question.id}
                  question={question}
                  answer={answers[question.id]}
                  onChange={(value) => setAnswer(question.id, value)}
                  error={validationErrors[question.id]}
                  collection={collection}
                  getDisplayValue={
                    getDisplayValue as (
                      questionId: number,
                      value: any,
                    ) => string[]
                  }
                />
              );
            })}
          </div>
          <ServicePickerButton
            onSubmit={handleSubmit}
            isLoading={isLoading}
            questions={questions || []}
            answers={answers}
          />
          {offer && <OfferDisplay offer={offer} />}
          {error && <div className={styles.error}>{error}</div>}
        </div>
        <SelectedState
          questions={questions || []}
          answers={answers}
          getDisplayValue={
            getDisplayValue as (questionId: number, value: any) => string[]
          }
        />
      </div>
    </div>
  );
};

export default App;
