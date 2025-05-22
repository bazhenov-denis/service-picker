import { useState } from "react";
import { serviceHttpClient } from "../client/serviceHttpClient";
import { useProfessions } from "./useProfessions";
import { useRegions } from "./useRegions";

interface OfferDto {
  vacancyOffers: Array<{
    id: number;
    areaId: number;
    professionId: number;
    packageVolume: number;
    vacancyType: string;
    publicationPeriod: number;
    pricePerOne: number;
    pricePerPackage: number;
  }>;
  resumesAccessOffers: Array<{
    id: number;
    areaId: number;
    professionId: number;
    accessDuration: number;
    numberOfContacts: number;
    price: number;
  }>;
}

function findParentRegionId(regions: any[], targetId: string): string | null {
  for (const region of regions) {
    if (region.id === targetId) return null;
    if (region.items) {
      for (const subRegion of region.items) {
        if (subRegion.id === targetId) {
          return region.id;
        }
        const nestedResult = findParentRegionId([subRegion], targetId);
        if (nestedResult) return region.id;
      }
    }
  }
  return null;
}

export const useServicePicker = (questions: any[]) => {
  const [answers, setAnswers] = useState<{ [key: number]: any }>({});
  const [validationErrors, setValidationErrors] = useState<{ [key: number]: string }>({});
  const [offer, setOffer] = useState<OfferDto | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const { getOriginalId } = useProfessions();
  const { regions } = useRegions();

  const setAnswer = (questionId: number, value: any) => {
    setAnswers((prev) => ({ ...prev, [questionId]: value }));
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
        const answer = answers[question.id];
        if (!answer || (Array.isArray(answer) && answer.length === 0)) {
          newErrors[question.id] = "Обязательное поле";
        } else if (question.type === "input") {
          const num = parseInt(answer, 10);
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

  const handleSendData = async () => {
    if (!validate()) return;
    setIsLoading(true);
    setError(null);
    try {
      const rawRegionId = answers[1];
      const originalRegionId = getOriginalId(rawRegionId);
      let areaId = parseInt(originalRegionId);
      const parentRegion = findParentRegionId(regions, rawRegionId);
      if (parentRegion) {
        areaId = parseInt(getOriginalId(parentRegion));
      }
      const professionId = parseInt(getOriginalId(answers[2]));
      const amount = parseInt(answers[3]);
      const response = await serviceHttpClient.sendServiceRequest({
        professionId,
        amount,
        areaId,
      });
      setOffer(response);
    } catch (err) {
      setError("Ошибка при отправке данных");
    } finally {
      setIsLoading(false);
    }
  };

  return {
    answers,
    setAnswer,
    handleSendData,
    offer,
    error,
    isLoading,
    validationErrors,
  };
};
