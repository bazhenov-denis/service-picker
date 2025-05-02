import { useState } from "react";
import { serviceHttpClient } from "../api/serviceHttpClient";
import { useProfessions } from "./useProfessions";

interface ServicePickerData {
  regions: string[];
  professions: string[];
  vacanciesNumber: number;
}

interface Offer {
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

export const useServicePicker = () => {
  const [selectedRegions, setSelectedRegions] = useState<string[]>([]);
  const [selectedProfessions, setSelectedProfessions] = useState<string[]>([]);
  const [vacanciesNumber, setVacanciesNumber] = useState<number>(0);
  const [offer, setOffer] = useState<Offer | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const { getOriginalId } = useProfessions();

  const handleSendData = async (data: ServicePickerData) => {
    try {
      setIsLoading(true);
      setError(null);

      // Получаем чистые ID без префиксов
      const areaId = parseInt(getOriginalId(data.regions[0]));
      const professionId = parseInt(getOriginalId(data.professions[0]));

      const response = await serviceHttpClient.sendServiceRequest({
        professionId,
        amount: data.vacanciesNumber,
        areaId,
      });

      setOffer(response);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Произошла ошибка при отправке данных",
      );
    } finally {
      setIsLoading(false);
    }
  };

  return {
    selectedRegions,
    setSelectedRegions,
    selectedProfessions,
    setSelectedProfessions,
    vacanciesNumber,
    setVacanciesNumber,
    handleSendData,
    offer,
    error,
    isLoading,
  };
};
