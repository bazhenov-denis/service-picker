import React from 'react';
import { Button } from '@hh.ru/magritte-ui-button';
import '@hh.ru/magritte-ui-button/index.css';
import styles from './ServicePickerButton.module.css';

interface ServicePickerButtonProps {
  selectedRegions: string[];
  selectedProfessions: string[];
  vacanciesNumber: number;
  onSendData: (data: {
    regions: string[];
    professions: string[];
    vacanciesNumber: number;
  }) => void;
  isLoading?: boolean;
  error?: string | null;
}

export const ServicePickerButton: React.FC<ServicePickerButtonProps> = ({
  selectedRegions,
  selectedProfessions,
  vacanciesNumber,
  onSendData,
  isLoading = false,
  error = null
}) => {
  const handleClick = () => {
    const data = {
      regions: selectedRegions,
      professions: selectedProfessions,
      vacanciesNumber,
    };
    
    // Отправляем данные в консоль разработчика
    console.log('Отправка данных на бэкенд:', data);
    
    // Вызываем колбэк с данными
    onSendData(data);
  };

  const isDisabled = !selectedRegions.length || !selectedProfessions.length || !vacanciesNumber || isLoading;

  return (
    <div className={styles.buttonContainer}>
      <Button
        mode="primary"
        style="accent"
        size="large"
        stretched
        disabled={isDisabled}
        onClick={handleClick}
        data-qa="service-picker-button"
      >
        {isLoading ? 'Отправка...' : 'Подобрать услугу'}
      </Button>
      {error && <div className={styles.error}>{error}</div>}
    </div>
  );
};

export default ServicePickerButton; 