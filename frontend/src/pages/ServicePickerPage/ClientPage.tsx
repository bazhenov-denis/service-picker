import React from "react";
import { useServicePicker } from "./hooks/useServicePicker";
import { useQuestions } from "./hooks/useQuestions";
import { useRegions } from "./hooks/useRegions";
import { useProfessions } from "./hooks/useProfessions";
import { useQuestionSegments } from "./hooks/useQuestionSegments";
import QuestionRenderer from "./components/QuestionRenderer/QuestionRenderer";
import ServicePickerButton from "./components/ServicePickerButton/ServicePickerButton";
import OfferDisplay from "./components/OfferDisplay/OfferDisplay";
import ProgressNavigation from "./components/ProgressNavigation/ProgressNavigation";
import { SelectedState } from "./components/SelectedState/SelectedState";
import styles from "../../App.module.css";

const ClientPage: React.FC = () => {
  const {
    questions,
    isLoading: questionsLoading,
    error: questionsError,
  } = useQuestions("client");
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

  const {
    currentSegment,
    totalSegments,
    currentSegmentQuestions,
    canProceedToNext,
    isLastSegment,
    handleSegmentChange,
    QUESTIONS_PER_SEGMENT,
  } = useQuestionSegments(questions || [], answers);

  const scrollToQuestion = (questionId: number) => {
    const questionElement = document.getElementById(`question-${questionId}`);
    if (questionElement) {
      questionElement.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
    }
  };

  if (questionsLoading) {
    return <div>Загрузка вопросов...</div>;
  }

  if (questionsError) {
    return <div>Ошибка: {questionsError}</div>;
  }

  return (
    <div className={styles.container}>
      <div className={styles.mainContent}>
        <div className={styles.questionsContainer}>
          {currentSegmentQuestions.map((question) => {
            const collection =
              question.type === "reference" && question.referenceType === "regions"
                ? regionsCollection
                : question.type === "reference" && question.referenceType === "professions"
                ? professionsCollection
                : undefined;
            return (
              <div key={question.id} id={`question-${question.id}`}>
                <QuestionRenderer
                  question={question}
                  answer={answers[question.id]}
                  onChange={(value) => setAnswer(question.id, value)}
                  error={validationErrors[question.id]}
                  collection={collection}
                  getDisplayValue={getDisplayValue as (questionId: number, value: any) => string[]}
                />
              </div>
            );
          })}
        </div>
        {isLastSegment && (
          <ServicePickerButton
            onSubmit={handleSubmit}
            isLoading={isLoading}
            questions={questions || []}
            answers={answers}
          />
        )}
        <ProgressNavigation
          currentSegment={currentSegment}
          totalSegments={totalSegments}
          onSegmentChange={handleSegmentChange}
          canProceed={canProceedToNext}
          isLastSegment={isLastSegment}
        />
        {isLastSegment && offer && <OfferDisplay offer={offer} />}
        {error && <div className={styles.error}>{error}</div>}
      </div>
      <SelectedState
        questions={questions || []}
        answers={answers}
        getDisplayValue={getDisplayValue as (questionId: number, value: any) => string[]}
        onQuestionClick={scrollToQuestion}
        onSegmentChange={handleSegmentChange}
        currentSegment={currentSegment}
        questionsPerSegment={QUESTIONS_PER_SEGMENT}
      />
    </div>
  );
};

export default ClientPage; 