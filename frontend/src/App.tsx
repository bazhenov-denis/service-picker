import React from "react";
import Header from "./pages/ServicePickerPage/components/Header/Header";
import QuestionRenderer from "./pages/ServicePickerPage/components/QuestionRenderer/QuestionRenderer";
import ServicePickerButton from "./pages/ServicePickerPage/components/ServicePickerButton/ServicePickerButton";
import OfferDisplay from "./pages/ServicePickerPage/components/OfferDisplay/OfferDisplay";
import { useServicePicker } from "./pages/ServicePickerPage/hooks/useServicePicker";
import { useRegions } from "./pages/ServicePickerPage/hooks/useRegions";
import { useProfessions } from "./pages/ServicePickerPage/hooks/useProfessions";
import { useQuestions } from "./pages/ServicePickerPage/hooks/useQuestions";
import styles from "./App.module.css";

const App: React.FC = () => {
  const { questions } = useQuestions();
  const { collection: regionsCollection } = useRegions();
  const { collection: professionsCollection } = useProfessions();
  const {
    answers,
    setAnswer,
    handleSendData,
    offer,
    error,
    isLoading,
    validationErrors,
  } = useServicePicker(questions);

  return (
    <div className={styles.app}>
      <Header />
      <div className={styles.container}>
        {questions.map((question) => {
          let collection = null;
          if (question.referenceType === "regions") {
            collection = regionsCollection;
          } else if (question.referenceType === "professions") {
            collection = professionsCollection;
          }

          return (
            <QuestionRenderer
              key={question.id}
              question={question}
              answer={answers[question.id]}
              onChange={(value) => setAnswer(question.id, value)}
              error={validationErrors[question.id] || null}
              collection={collection}
            />
          );
        })}
        <ServicePickerButton
          onSendData={handleSendData}
          isLoading={isLoading}
          error={error}
          selectedRegions={answers[1] || []}
          selectedProfessions={answers[2] || []}
          vacanciesNumber={answers[3] || []}
        />
        {offer && <OfferDisplay offer={offer} />}
      </div>
    </div>
  );
};

export default App;
