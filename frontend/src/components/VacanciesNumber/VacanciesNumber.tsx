import React, { useState } from 'react';
import { Input } from '@hh.ru/magritte-ui-input';
import styles from './VacanciesNumber.module.css';

const VacanciesNumber: React.FC = () => {
  const [value, setValue] = useState<string>('');
  const [error, setError] = useState<string>('');

  const handleChange = (inputValue: string) => {
    // Разрешаем только цифры
    if (!/^\d*$/.test(inputValue)) {
      setError('Пожалуйста, введите только цифры');
      return;
    }

    // Проверяем, что число больше 0
    const numValue = parseInt(inputValue);
    if (inputValue && numValue <= 0) {
      setError('Число должно быть больше 0');
      return;
    }

    setValue(inputValue);
    setError('');
  };

  return (
    <div className={styles.vacanciesNumber}>
      <h2 className={styles.title}>Количество вакансий</h2>
      <Input
        value={value}
        onChange={handleChange}
        placeholder="Введите количество вакансий"
        errorMessage={error}
        invalid={!!error}
      />
    </div>
  );
};

export default VacanciesNumber; 