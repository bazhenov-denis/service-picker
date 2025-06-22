import React, { useState, useMemo, useEffect } from "react";
import { GridLayout, GridRow, GridColumn } from "@hh.ru/magritte-ui-grid";
import { Routes, Route, useLocation, Link } from "react-router-dom";
import QuestionRenderer from "./pages/ServicePickerPage/components/QuestionRenderer/QuestionRenderer";
import ServicePickerButton from "./pages/ServicePickerPage/components/ServicePickerButton/ServicePickerButton";
import OfferDisplay from "./pages/ServicePickerPage/components/OfferDisplay/OfferDisplay";
import { SelectedState } from "./pages/ServicePickerPage/components/SelectedState/SelectedState";
import ProgressNavigation from "./pages/ServicePickerPage/components/ProgressNavigation/ProgressNavigation";
import { useServicePicker } from "./pages/ServicePickerPage/hooks/useServicePicker";
import { useRegions } from "./pages/ServicePickerPage/hooks/useRegions";
import { useProfessions } from "./pages/ServicePickerPage/hooks/useProfessions";
import { useQuestions } from "./pages/ServicePickerPage/hooks/useQuestions";
import styles from "./App.module.css";
import type { ReferenceQuestion } from "./pages/ServicePickerPage/types/question";
import type { Question } from "./pages/ServicePickerPage/types/question";
import Header from "./pages/ServicePickerPage/components/Header/Header";
import { PenOutlinedSize24, EyeOutlinedSize24, EyeCrossedOutlinedSize24, ArrowUpOutlinedSize24, ArrowDownOutlinedSize24, CrossOutlinedSize24 } from '@hh.ru/magritte-ui-icon/variants/icon';

const QUESTIONS_PER_SEGMENT = 3;

const App: React.FC = () => {
  const location = useLocation();
  const headerTitle =
    location.pathname === "/admin" ? "Админ-панель" : "Подборщик услуг";

  const questionsRole = location.pathname === "/admin" ? "admin" : "client";
  const {
    questions,
    isLoading: questionsLoading,
    error: questionsError,
  } = useQuestions(questionsRole);
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

  const [currentSegment, setCurrentSegment] = useState(0);
  const [editModeId, setEditModeId] = useState<number | null>(null);
  const [adminQuestions, setAdminQuestions] = useState<Question[] | null>(null);

  const totalSegments = useMemo(() => {
    return Math.ceil((questions?.length || 0) / QUESTIONS_PER_SEGMENT);
  }, [questions]);

  const currentSegmentQuestions = useMemo(() => {
    if (!questions) return [];
    const startIndex = currentSegment * QUESTIONS_PER_SEGMENT;
    const endIndex = startIndex + QUESTIONS_PER_SEGMENT;
    return questions.slice(startIndex, endIndex);
  }, [questions, currentSegment]);

  const canProceedToNext = useMemo(() => {
    return currentSegmentQuestions.every((question) => {
      if (!question.isRequired) return true;
      const answer = answers[question.id];
      return answer && (Array.isArray(answer) ? answer.length > 0 : true);
    });
  }, [currentSegmentQuestions, answers]);

  const isLastSegment = currentSegment === totalSegments - 1;

  const scrollToQuestion = (questionId: number) => {
    const questionElement = document.getElementById(`question-${questionId}`);
    if (questionElement) {
      questionElement.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
    }
  };

  const handleSegmentChange = (newSegment: number) => {
    setCurrentSegment(newSegment);
  };

  useEffect(() => {
    if (location.pathname === "/admin" && questions) {
      setAdminQuestions([...questions]);
    }
  }, [questions, location.pathname]);

  function handleMoveQuestionUp(idx: number) {
    if (!adminQuestions || idx === 0) return;
    const newQuestions = [...adminQuestions];
    [newQuestions[idx - 1], newQuestions[idx]] = [newQuestions[idx], newQuestions[idx - 1]];
    setAdminQuestions(newQuestions.map((q, i) => ({ ...q, position: i + 1 })));
  }

  function handleMoveQuestionDown(idx: number) {
    if (!adminQuestions || idx === adminQuestions.length - 1) return;
    const newQuestions = [...adminQuestions];
    [newQuestions[idx], newQuestions[idx + 1]] = [newQuestions[idx + 1], newQuestions[idx]];
    setAdminQuestions(newQuestions.map((q, i) => ({ ...q, position: i + 1 })));
  }

  function handleToggleActive(idx: number) {
    if (!adminQuestions) return;
    const newQuestions = [...adminQuestions];
    newQuestions[idx] = { ...newQuestions[idx], active: !newQuestions[idx].active };
    setAdminQuestions(newQuestions);
  }

  if (questionsLoading && location.pathname !== "/admin") {
    return <div>Загрузка вопросов...</div>;
  }

  if (questionsError && location.pathname !== "/admin") {
    return <div>Ошибка: {questionsError}</div>;
  }

  function renderServicePickerContent({
    showSelectedState,
    showProgress,
    showButton,
    allQuestions,
  }: {
    showSelectedState: boolean;
    showProgress: boolean;
    showButton: boolean;
    allQuestions: boolean;
  }) {
    const questionsToRender = location.pathname === "/admin"
      ? adminQuestions || []
      : allQuestions
        ? questions || []
        : currentSegmentQuestions;
    return (
      <div className={styles.container}>
        <GridLayout>
          <GridRow>
            <GridColumn xs={4} s={2} m={2} l={2} xl={2} xxl={2}>
              {/* Пустые колонки слева */}
            </GridColumn>
            <GridColumn xs={4} s={4} m={4} l={4} xl={4} xxl={4}>
              <div className={styles.mainContent}>
                <div className={styles.questionsContainer}>
                  {questionsToRender.map((question, idx) => {
                    const collection =
                      question.type === "reference" &&
                      (question as ReferenceQuestion).referenceType ===
                        "regions"
                        ? regionsCollection
                        : question.type === "reference" &&
                            (question as ReferenceQuestion).referenceType ===
                              "professions"
                          ? professionsCollection
                          : undefined;
                    const isAdmin = location.pathname === "/admin";
                    return (
                      <div key={question.id} id={`question-${question.id}`} style={{ display: 'flex', alignItems: 'center', marginBottom: 16 }}>
                        <div style={{ flex: 1 }} className={isAdmin && !question.active ? styles.inactiveQuestion : undefined}>
                          <QuestionRenderer
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
                        </div>
                        {isAdmin && (
                          <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', gap: 8, marginLeft: 16 }}>
                            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4, minHeight: 144, justifyContent: 'center' }}>
                              {editModeId === question.id && (
                                <button
                                  className={styles.adminEditButton}
                                  aria-label="Вверх"
                                  disabled={idx === 0}
                                  onClick={() => handleMoveQuestionUp(idx)}
                                  style={{ cursor: idx === 0 ? 'not-allowed' : 'pointer', background: 'none', border: 'none', padding: 0 }}
                                >
                                  <ArrowUpOutlinedSize24
                                    initialColor={idx === 0 ? 'secondary' : 'primary'}
                                    backgroundStyle={'primary'}
                                    borderRadius={12}
                                    shadow="level-1"
                                    padding={16}
                                    increaseShadow={true}
                                  />
                                </button>
                              )}
                              <button
                                className={styles.adminEditButton}
                                aria-label={editModeId === question.id ? "Закрыть режим редактирования" : "Редактировать"}
                                onClick={() => setEditModeId(editModeId === question.id ? null : question.id)}
                                style={{ background: 'none', border: 'none', padding: 0 }}
                              >
                                {editModeId === question.id ? (
                                  <CrossOutlinedSize24
                                    initialColor="primary"
                                    backgroundStyle={'primary'}
                                    borderRadius={12}
                                    shadow="level-1"
                                    padding={16}
                                    increaseShadow={true}
                                  />
                                ) : (
                                  <PenOutlinedSize24
                                    initialColor="primary"
                                    backgroundStyle={'primary'}
                                    borderRadius={12}
                                    shadow="level-1"
                                    padding={16}
                                    increaseShadow={true}
                                  />
                                )}
                              </button>
                              {editModeId === question.id && (
                                <button
                                  className={styles.adminEditButton}
                                  aria-label="Вниз"
                                  disabled={idx === questionsToRender.length - 1}
                                  onClick={() => handleMoveQuestionDown(idx)}
                                  style={{ cursor: idx === questionsToRender.length - 1 ? 'not-allowed' : 'pointer', background: 'none', border: 'none', padding: 0 }}
                                >
                                  <ArrowDownOutlinedSize24
                                    initialColor={idx === questionsToRender.length - 1 ? 'secondary' : 'primary'}
                                    backgroundStyle={'primary'}
                                    borderRadius={12}
                                    shadow="level-1"
                                    padding={16}
                                    increaseShadow={true}
                                  />
                                </button>
                              )}
                            </div>
                            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: 144 }}>
                              {editModeId === question.id && (
                                <button
                                  className={styles.adminEditButton}
                                  aria-label={question.active ? "Сделать неактивным" : "Сделать активным"}
                                  onClick={() => handleToggleActive(idx)}
                                  style={{ background: 'none', border: 'none', padding: 0 }}
                                >
                                  {question.active ? (
                                    <EyeOutlinedSize24
                                      initialColor="primary"
                                      backgroundStyle={'primary'}
                                      borderRadius={12}
                                      shadow="level-1"
                                      padding={16}
                                      increaseShadow={true}
                                    />
                                  ) : (
                                    <EyeCrossedOutlinedSize24
                                      initialColor="secondary"
                                      backgroundStyle={'primary'}
                                      borderRadius={12}
                                      shadow="level-1"
                                      padding={16}
                                      increaseShadow={true}
                                    />
                                  )}
                                </button>
                              )}
                            </div>
                          </div>
                        )}
                      </div>
                    );
                  })}
                </div>
                {showButton && isLastSegment && (
                  <ServicePickerButton
                    onSubmit={handleSubmit}
                    isLoading={isLoading}
                    questions={questions || []}
                    answers={answers}
                  />
                )}
                {showProgress && (
                  <ProgressNavigation
                    currentSegment={currentSegment}
                    totalSegments={totalSegments}
                    onSegmentChange={handleSegmentChange}
                    canProceed={canProceedToNext}
                    isLastSegment={isLastSegment}
                  />
                )}
                {isLastSegment && offer && <OfferDisplay offer={offer} />}
                {error && <div className={styles.error}>{error}</div>}
              </div>
            </GridColumn>
            <GridColumn xs={4} s={1} m={1} l={1} xl={1} xxl={1}>
              {/* Пустая колонка между контентом */}
            </GridColumn>
            {showSelectedState && (
              <GridColumn xs={4} s={2} m={2} l={2} xl={2} xxl={2}>
                <SelectedState
                  questions={questions || []}
                  answers={answers}
                  getDisplayValue={
                    getDisplayValue as (
                      questionId: number,
                      value: any,
                    ) => string[]
                  }
                  onQuestionClick={scrollToQuestion}
                  onSegmentChange={handleSegmentChange}
                  currentSegment={currentSegment}
                  questionsPerSegment={QUESTIONS_PER_SEGMENT}
                />
              </GridColumn>
            )}
            <GridColumn xs={4} s={5} m={5} l={5} xl={5} xxl={5}>
              {/* Пустые колонки справа */}
            </GridColumn>
          </GridRow>
        </GridLayout>
      </div>
    );
  }

  return (
    <div className={styles.app}>
      <Header title={headerTitle} />
      <Routes>
        <Route
          path="/"
          element={renderServicePickerContent({
            showSelectedState: true,
            showProgress: true,
            showButton: true,
            allQuestions: false,
          })}
        />
        <Route
          path="/admin"
          element={renderServicePickerContent({
            showSelectedState: false,
            showProgress: false,
            showButton: false,
            allQuestions: true,
          })}
        />
      </Routes>
    </div>
  );
};

export default App;
