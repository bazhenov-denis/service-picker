import { useState, useEffect } from "react";
import { useRegions } from "./useRegions";
import { useProfessions } from "./useProfessions";
import { sendAnswers } from "../client/httpClient";
import type { OfferDto } from "../types/service";
import type { SingleChoiceQuestion } from "../types/question";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { TreeModel } from "@hh.ru/magritte-ui-tree-selector/collection/types";

type AnswerValue = string[] | number[] | number;

const regionPathMap = new Map<string, string>();
const regionIdMap = new Map<string, string>();

const findRegionPath = (
  collection: TreeCollection,
  targetId: string,
): string[] => {
  const model = collection.getModel(targetId);
  if (!model) return [];

  const path: string[] = [];
  let currentId: string | undefined = targetId;

  while (currentId) {
    path.unshift(currentId);
    const parent = collection.getParent(currentId);
    currentId = parent?.id;
  }

  return path;
};

const updateRegionMaps = (collection: TreeCollection) => {
  console.log("Начало updateRegionMaps");

  regionPathMap.clear();
  regionIdMap.clear();

  const models: TreeModel[] = [];
  const processedIds = new Set<string>();

  const processModel = (modelId: string) => {
    if (processedIds.has(modelId)) return;

    const model = collection.getModel(modelId);
    if (!model) {
      console.warn(`Модель не найдена для ID: ${modelId}`);
      return;
    }

    processedIds.add(modelId);
    models.push(model);

    const children = collection.getChildren(modelId);
    if (children) {
      console.log(
        `Найдены дочерние элементы для ${modelId}:`,
        children.map((c) => c.id),
      );
      children.forEach((child) => processModel(child.id));
    } else {
      console.log(`Нет дочерних элементов для ${modelId}`);
    }
  };

  const rootModel = collection.getModel("root") || collection.getModel("1");
  if (rootModel) {
    console.log("Начинаем с корневой модели:", rootModel.id);
    processModel(rootModel.id);
  } else {
    const anyModel = collection.getModel("113");
    if (anyModel) {
      console.log("Начинаем с модели:", anyModel.id);
      processModel(anyModel.id);
    } else {
      console.error("Не найдено ни одной модели в коллекции");
      return;
    }
  }

  console.log("Обработано моделей:", models.length);

  models.forEach((model) => {
    const path = findRegionPath(collection, model.id).join(".");
    if (path) {
      regionPathMap.set(model.id, path);
      regionIdMap.set(path, model.id);
      console.log(`Добавлен маппинг: ${model.id} -> ${path}`);
    } else {
      console.warn(`Не удалось построить путь для модели: ${model.id}`);
    }
  });

  console.log("Маппинги обновлены:", {
    totalModels: models.length,
    pathMapSize: regionPathMap.size,
    idMapSize: regionIdMap.size,
    sampleEntries: Array.from(regionPathMap.entries()).slice(0, 3),
  });
};

export const useServicePicker = (questions: any[]) => {
  const [answers, setAnswers] = useState<Record<string, AnswerValue>>({});
  const [validationErrors, setValidationErrors] = useState<{
    [key: number]: string;
  }>({});
  const [offer, setOffer] = useState<OfferDto | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const { collection: regionsCollection } = useRegions();
  const { collection: professionsCollection, getOriginalId } = useProfessions();

  const setAnswer = (questionId: number, value: any) => {
    let formattedValue: AnswerValue;
    const question = questions.find((q) => q.id === questionId);

    console.log("setAnswer вызван с:", {
      questionId,
      value,
      questionType: question?.type,
      referenceType: question?.referenceType,
    });

    if (Array.isArray(value)) {
      if (
        question?.type === "reference" &&
        question?.referenceType === "regions"
      ) {
        formattedValue = value.map((id) => {
          const path = findRegionPath(regionsCollection, String(id)).join(".");
          return path || String(id);
        });
      } else {
        formattedValue = value;
      }
    } else if (typeof value === "number") {
      formattedValue = [value];
    } else {
      console.error("Неизвестный тип значения:", value);
      return;
    }

    setAnswers((prev) => {
      const newAnswers = { ...prev, [questionId.toString()]: formattedValue };
      console.log("Новое состояние answers:", newAnswers);
      return newAnswers;
    });

    if (validationErrors[questionId]) {
      setValidationErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[questionId];
        return newErrors;
      });
    }
  };

  const validate = () => {
    const newErrors: { [key: number]: string } = {};
    questions.forEach((question) => {
      if (question.isRequired) {
        const answer = answers[question.id.toString()];
        if (!answer || (Array.isArray(answer) && answer.length === 0)) {
          newErrors[question.id] = "Обязательное поле";
        } else if (question.type === "input") {
          const num = Array.isArray(answer)
            ? Number(answer[0])
            : Number(answer);
          const min = question.validation?.min ?? 1;
          const max = question.validation?.max ?? 100;
          if (isNaN(num) || num < min || num > max) {
            newErrors[question.id] = `Введите число от ${min} до ${max}`;
          }
        }
      }
    });
    setValidationErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validate()) {
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      console.log("Начало отправки данных. Текущие состояния:", {
        answers,
        regionPathMapSize: regionPathMap.size,
      });

      const answersToSend = Object.entries(answers).reduce(
        (acc, [key, value]) => {
          const question = questions.find((q) => q.id.toString() === key);
          
          console.log(`Обработка вопроса ${key}:`, {
            value,
            questionType: question?.type,
            referenceType: question?.referenceType
          });

          if (!question) return acc;

          let stringValue: string;

          if (question.type === "reference") {
            if (question.referenceType === "regions") {
              if (Array.isArray(value)) {
                stringValue = value[0].toString();
                console.log(`Регион: преобразование ${value[0]} -> ${stringValue}`);
              } else {
                const path = findRegionPath(
                  regionsCollection,
                  String(value),
                ).join(".");
                stringValue = path || String(value);
                console.log(`Регион: преобразование ${value} -> ${stringValue}`);
              }
            } else if (question.referenceType === "professions") {
              const extractRoleId = (path: string) => {
                const categoryMatch = path.match(/category_(\d+)$/);
                if (categoryMatch) {
                  console.log(`Найдена категория в профессиях: ${path} -> ${categoryMatch[1]}`);
                  return categoryMatch[1];
                }
                const roleMatch = path.match(/role_(\d+)$/);
                return roleMatch ? roleMatch[1] : path;
              };

              stringValue = Array.isArray(value)
                ? extractRoleId(String(value[0]))
                : extractRoleId(String(value));
              console.log(`Профессия/Категория: преобразование ${value} -> ${stringValue}`);
            } else {
              const extractCategoryId = (path: string) => {
                const match = path.match(/category_(\d+)$/);
                const result = match ? match[1] : path;
                console.log(`Категория: преобразование ${path} -> ${result}`);
                return result;
              };

              stringValue = Array.isArray(value)
                ? extractCategoryId(String(value[0]))
                : extractCategoryId(String(value));
            }
          } else {
            stringValue = Array.isArray(value)
              ? String(value[0])
              : String(value);
            console.log(`Обычное значение: преобразование ${value} -> ${stringValue}`);
          }

          acc[question.id.toString()] = [stringValue];
          console.log(`Итоговое значение для вопроса ${key}:`, [stringValue]);
          return acc;
        },
        {} as Record<string, string[]>,
      );

      console.log("Финальные данные для отправки:", answersToSend);
      const result = await sendAnswers(answersToSend);
      setOffer(result);
    } catch (err) {
      console.error("Ошибка при отправке ответов:", err);
      setError("Не удалось отправить ответы");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (regionsCollection) {
      console.log("Коллекция регионов обновлена:", {
        hasCollection: !!regionsCollection,
        collectionType: regionsCollection.constructor.name,
      });

      const timer = setTimeout(() => {
        updateRegionMaps(regionsCollection);
      }, 100);

      return () => clearTimeout(timer);
    }
  }, [regionsCollection]);

  const getDisplayValue = (
    questionId: number,
    value: AnswerValue,
  ): string[] => {
    const question = questions.find(
      (q) => q.id.toString() === questionId.toString(),
    );
    if (question?.type === "reference" && Array.isArray(value)) {
      return value.map((path) => {
        const lastId = String(path).split(".").pop();
        if (!lastId) return String(path);

        if (question.referenceType === "regions") {
          const model = regionsCollection.getModel(lastId);
          return model?.text || String(path);
        }
        if (question.referenceType === "professions") {
          const model = professionsCollection.getModel(lastId);
          return model?.text || String(path);
        }
        return String(path);
      });
    }
    if (
      question?.type === "single-choice" &&
      Array.isArray(value) &&
      value.length > 0
    ) {
      const option = (question as SingleChoiceQuestion).options?.find(
        (opt: { id: number; text: string }) => opt.id === value[0],
      );
      return option ? [option.text] : [];
    }
    if (question?.type === "multiple-choice" && Array.isArray(value)) {
      return value.map((val) => {
        const option = (question as any).options?.find(
          (opt: { id: number; text: string }) => opt.id === val,
        );
        return option ? option.text : String(val);
      });
    }
    if (question?.type === "input") {
      return [Array.isArray(value) ? String(value[0]) : String(value)];
    }
    return Array.isArray(value) ? value.map(String) : [];
  };

  return {
    answers,
    setAnswer,
    validationErrors,
    offer,
    error,
    isLoading,
    handleSubmit,
    getDisplayValue,
  };
};
