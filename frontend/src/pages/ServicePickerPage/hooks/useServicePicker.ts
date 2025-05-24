import { useState, useEffect } from "react";
import { useRegions } from "./useRegions";
import { sendAnswers } from "../client/httpClient";
import type { OfferDto } from "../types/service";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { TreeModel } from "@hh.ru/magritte-ui-tree-selector/collection/types";

type AnswerValue = string[] | number[] | number;

const regionPathMap = new Map<string, string>();
const regionIdMap = new Map<string, string>();

const findRegionPath = (collection: TreeCollection, targetId: string): string[] => {
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
  console.log('Начало updateRegionMaps');
  
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
      console.log(`Найдены дочерние элементы для ${modelId}:`, children.map(c => c.id));
      children.forEach(child => processModel(child.id));
    } else {
      console.log(`Нет дочерних элементов для ${modelId}`);
    }
  };

  const rootModel = collection.getModel('root') || collection.getModel('1');
  if (rootModel) {
    console.log('Начинаем с корневой модели:', rootModel.id);
    processModel(rootModel.id);
  } else {
    const anyModel = collection.getModel('113');
    if (anyModel) {
      console.log('Начинаем с модели:', anyModel.id);
      processModel(anyModel.id);
    } else {
      console.error('Не найдено ни одной модели в коллекции');
      return;
    }
  }

  console.log('Обработано моделей:', models.length);

  models.forEach(model => {
    const path = findRegionPath(collection, model.id).join('.');
    if (path) {
      regionPathMap.set(model.id, path);
      regionIdMap.set(path, model.id);
      console.log(`Добавлен маппинг: ${model.id} -> ${path}`);
    } else {
      console.warn(`Не удалось построить путь для модели: ${model.id}`);
    }
  });

  console.log('Маппинги обновлены:', {
    totalModels: models.length,
    pathMapSize: regionPathMap.size,
    idMapSize: regionIdMap.size,
    sampleEntries: Array.from(regionPathMap.entries()).slice(0, 3)
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

  const setAnswer = (questionId: number, value: any) => {
    let formattedValue: AnswerValue;
    const question = questions.find(q => q.id === questionId);
    
    console.log('setAnswer вызван с:', { questionId, value, questionType: question?.type, referenceType: question?.referenceType });
    
    if (Array.isArray(value)) {
      if (question?.type === "reference" && question?.referenceType === "regions") {
        const paths = value.map(id => {
          const path = findRegionPath(regionsCollection, String(id)).join('.');
          if (!path) {
            console.warn(`Не удалось построить путь для региона с ID ${id}`);
            return String(id);
          }
          return path;
        });
        
        console.log('Сохранение путей регионов:', { questionId, paths, value });
        formattedValue = paths;
      } else {
        formattedValue = value;
      }
    } else if (typeof value === 'number') {
      formattedValue = [value];
    } else {
      console.error('Неизвестный тип значения:', value);
      return;
    }

    setAnswers(prev => {
      const newAnswers = { ...prev, [questionId.toString()]: formattedValue };
      console.log('Новое состояние answers:', newAnswers);
      return newAnswers;
    });
    
    if (validationErrors[questionId]) {
      setValidationErrors(prev => {
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
          const num = Array.isArray(answer) ? Number(answer[0]) : Number(answer);
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
      console.log('Начало отправки данных. Текущие состояния:', {
        answers,
        regionPathMapSize: regionPathMap.size
      });

      const answersToSend = { ...answers };
      console.log('Финальные данные для отправки:', answersToSend);
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
      console.log('Коллекция регионов обновлена:', {
        hasCollection: !!regionsCollection,
        collectionType: regionsCollection.constructor.name
      });
      
      const timer = setTimeout(() => {
        updateRegionMaps(regionsCollection);
      }, 100);

      return () => clearTimeout(timer);
    }
  }, [regionsCollection]);

  const getDisplayValue = (questionId: number, value: AnswerValue): string[] => {
    const question = questions.find(q => q.id.toString() === questionId.toString());
    if (question?.type === "reference" && question?.referenceType === "regions" && Array.isArray(value)) {
      return value.map(path => {
        const parts = String(path).split('.');
        return parts[parts.length - 1];
      });
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
