import { useState } from 'react';

interface ServicePickerData {
  regions: string[];
  professions: string[];
  vacanciesNumber: number;
}

export const useServicePicker = () => {
  const [selectedRegions, setSelectedRegions] = useState<string[]>([]);
  const [selectedProfessions, setSelectedProfessions] = useState<string[]>([]);
  const [vacanciesNumber, setVacanciesNumber] = useState<number>(0);

  const handleSendData = (data: ServicePickerData) => {
    console.log('Данные отправлены:', data);
  };

  return {
    selectedRegions,
    setSelectedRegions,
    selectedProfessions,
    setSelectedProfessions,
    vacanciesNumber,
    setVacanciesNumber,
    handleSendData
  };
}; 