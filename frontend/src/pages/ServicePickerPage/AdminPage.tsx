import React from "react";
import { useAdminQuestions } from "./hooks/useAdminQuestions";
import QuestionRenderer from "./components/QuestionRenderer/QuestionRenderer";
import styles from "../../App.module.css";
import {
  PenOutlinedSize24,
  EyeOutlinedSize24,
  EyeCrossedOutlinedSize24,
  ArrowUpOutlinedSize24,
  ArrowDownOutlinedSize24,
  CrossOutlinedSize24,
} from "@hh.ru/magritte-ui-icon/variants/icon";

const AdminPage: React.FC = () => {
  const {
    adminQuestions,
    setAdminQuestions,
    editModeId,
    setEditModeId,
    scoreTypes,
    handleMoveQuestionUp,
    handleMoveQuestionDown,
    handleToggleActive,
  } = useAdminQuestions();

  return (
    <div className={styles.container}>
      <div className={styles.mainContent}>
        <div className={styles.questionsContainer}>
          {adminQuestions
            .filter((q) => q.isRequired === false)
            .map((question, idx) => (
              <div
                key={question.id}
                id={`question-${question.id}`}
                style={{
                  display: "flex",
                  alignItems: "center",
                  marginBottom: 16,
                }}
              >
                <div
                  style={{ flex: 1 }}
                  className={
                    !question.active ? styles.inactiveQuestion : undefined
                  }
                >
                  <QuestionRenderer
                    question={question}
                    isAdmin={true}
                    scoreTypes={scoreTypes}
                    answer={undefined}
                    onChange={() => {}}
                    error={null}
                    onQuestionTextChange={(newText) => {
                      const realIdx = adminQuestions.findIndex(
                        (q) => q.id === question.id,
                      );
                      if (realIdx === -1) return;
                      const newQuestions = [...adminQuestions];
                      newQuestions[realIdx] = {
                        ...question,
                        questionText: newText,
                      };
                      setAdminQuestions(newQuestions);
                    }}
                    onOptionChange={(optIdx, field, value) => {
                      if (
                        question.type !== "single-choice" &&
                        question.type !== "multiple-choice"
                      )
                        return;
                      const realIdx = adminQuestions.findIndex(
                        (q) => q.id === question.id,
                      );
                      if (realIdx === -1) return;
                      const newQuestions = [...adminQuestions];
                      const newOptions = [...(question.options || [])];
                      if (field === "text") {
                        newOptions[optIdx] = {
                          ...newOptions[optIdx],
                          text: value,
                        };
                      } else if (field === "scoreType") {
                        const selectedType = scoreTypes.find(
                          (st) => st.code === value,
                        );
                        const newScores = newOptions[optIdx].scores
                          ? [...newOptions[optIdx].scores]
                          : [
                              {
                                id: newOptions[optIdx].id,
                                code: "",
                                title: "",
                                weight: 0,
                              },
                            ];
                        newScores[0] = {
                          ...newScores[0],
                          code: selectedType?.code || "",
                          title: selectedType?.title || "",
                        };
                        newOptions[optIdx] = {
                          ...newOptions[optIdx],
                          scores: newScores,
                        };
                      } else if (field === "weight") {
                        const newScores = newOptions[optIdx].scores
                          ? [...newOptions[optIdx].scores]
                          : [
                              {
                                id: newOptions[optIdx].id,
                                code: "",
                                title: "",
                                weight: 0,
                              },
                            ];
                        newScores[0] = {
                          ...newScores[0],
                          weight: value === "" ? 0 : Number(value),
                        };
                        newOptions[optIdx] = {
                          ...newOptions[optIdx],
                          scores: newScores,
                        };
                      }
                      newQuestions[realIdx] = {
                        ...question,
                        options: newOptions,
                      };
                      setAdminQuestions(newQuestions);
                    }}
                    onOptionAdd={() => {
                      if (
                        question.type !== "single-choice" &&
                        question.type !== "multiple-choice"
                      )
                        return;
                      const realIdx = adminQuestions.findIndex(
                        (q) => q.id === question.id,
                      );
                      if (realIdx === -1) return;
                      const newQuestions = [...adminQuestions];
                      const newOptions = [
                        ...(question.options || []),
                        {
                          id: Date.now(),
                          text: "",
                          scores: [
                            {
                              id: Date.now(),
                              code: scoreTypes[0]?.code || "",
                              title: scoreTypes[0]?.title || "",
                              weight: 0,
                            },
                          ],
                        },
                      ];
                      newQuestions[realIdx] = {
                        ...question,
                        options: newOptions,
                      };
                      setAdminQuestions(newQuestions);
                    }}
                    onOptionRemove={(optIdx) => {
                      if (
                        question.type !== "single-choice" &&
                        question.type !== "multiple-choice"
                      )
                        return;
                      const realIdx = adminQuestions.findIndex(
                        (q) => q.id === question.id,
                      );
                      if (realIdx === -1) return;
                      const newQuestions = [...adminQuestions];
                      const newOptions = (question.options || []).filter(
                        (_, i) => i !== optIdx,
                      );
                      newQuestions[realIdx] = {
                        ...question,
                        options: newOptions,
                      };
                      setAdminQuestions(newQuestions);
                    }}
                  />
                </div>
                <div
                  style={{
                    display: "flex",
                    flexDirection: "row",
                    alignItems: "center",
                    gap: 8,
                    marginLeft: 16,
                  }}
                >
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      alignItems: "center",
                      gap: 4,
                      minHeight: 144,
                      justifyContent: "center",
                    }}
                  >
                    {editModeId === question.id && (
                      <button
                        className={styles.adminEditButton}
                        aria-label="Вверх"
                        disabled={idx === 0}
                        onClick={() => handleMoveQuestionUp(question.id)}
                        style={{
                          cursor: idx === 0 ? "not-allowed" : "pointer",
                          background: "none",
                          border: "none",
                          padding: 0,
                        }}
                      >
                        <ArrowUpOutlinedSize24
                          initialColor={idx === 0 ? "secondary" : "primary"}
                          backgroundStyle={"primary"}
                          borderRadius={12}
                          shadow="level-1"
                          padding={16}
                          increaseShadow={true}
                        />
                      </button>
                    )}
                    <button
                      className={styles.adminEditButton}
                      aria-label={
                        editModeId === question.id
                          ? "Закрыть режим редактирования"
                          : "Редактировать"
                      }
                      onClick={() =>
                        setEditModeId(
                          editModeId === question.id ? null : question.id,
                        )
                      }
                      style={{ background: "none", border: "none", padding: 0 }}
                    >
                      {editModeId === question.id ? (
                        <CrossOutlinedSize24
                          initialColor="primary"
                          backgroundStyle={"primary"}
                          borderRadius={12}
                          shadow="level-1"
                          padding={16}
                          increaseShadow={true}
                        />
                      ) : (
                        <PenOutlinedSize24
                          initialColor="primary"
                          backgroundStyle={"primary"}
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
                        disabled={
                          idx ===
                          adminQuestions.filter((q) => q.isRequired === false)
                            .length -
                            1
                        }
                        onClick={() => handleMoveQuestionDown(question.id)}
                        style={{
                          cursor:
                            idx ===
                            adminQuestions.filter((q) => q.isRequired === false)
                              .length -
                              1
                              ? "not-allowed"
                              : "pointer",
                          background: "none",
                          border: "none",
                          padding: 0,
                        }}
                      >
                        <ArrowDownOutlinedSize24
                          initialColor={
                            idx ===
                            adminQuestions.filter((q) => q.isRequired === false)
                              .length -
                              1
                              ? "secondary"
                              : "primary"
                          }
                          backgroundStyle={"primary"}
                          borderRadius={12}
                          shadow="level-1"
                          padding={16}
                          increaseShadow={true}
                        />
                      </button>
                    )}
                  </div>
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      alignItems: "center",
                      justifyContent: "center",
                      minHeight: 144,
                    }}
                  >
                    {editModeId === question.id && (
                      <button
                        className={styles.adminEditButton}
                        aria-label={
                          question.active
                            ? "Сделать неактивным"
                            : "Сделать активным"
                        }
                        onClick={() => handleToggleActive(question.id)}
                        style={{
                          background: "none",
                          border: "none",
                          padding: 0,
                        }}
                      >
                        {question.active ? (
                          <EyeOutlinedSize24
                            initialColor="primary"
                            backgroundStyle={"primary"}
                            borderRadius={12}
                            shadow="level-1"
                            padding={16}
                            increaseShadow={true}
                          />
                        ) : (
                          <EyeCrossedOutlinedSize24
                            initialColor="secondary"
                            backgroundStyle={"primary"}
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
              </div>
            ))}
        </div>
      </div>
    </div>
  );
};

export default AdminPage;
