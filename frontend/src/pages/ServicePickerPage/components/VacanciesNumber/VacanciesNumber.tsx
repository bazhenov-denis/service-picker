import React from 'react';
import styles from './VacanciesNumber.module.css';

interface VacanciesNumberProps {
  onNumberChange: (number: number) => void;
}

export const VacanciesNumber: React.FC<VacanciesNumberProps> = ({ onNumberChange }) => {
  const [number, setNumber] = React.useState<string>('');
  const [error, setError] = React.useState<string>('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    
    // Если поле пустое, очищаем ошибку и значение
    if (value === '') {
      setNumber('');
      setError('');
      onNumberChange(0);
      return;
    }

    // Проверяем, что введено число
    const numValue = parseInt(value);
    if (isNaN(numValue)) {
      setError('Пожалуйста, введите число');
      return;
    }

    // Проверяем, что число больше 0
    if (numValue <= 0) {
      setError('Число должно быть больше 0');
      return;
    }

    setNumber(value);
    setError('');
    onNumberChange(numValue);
  };

  return (
    <div className={styles.vacanciesNumber}>
      <h2 className={styles.title}>Количество вакансий</h2>
      <input
        type="number"
        className={styles.input}
        value={number}
        onChange={handleChange}
        min="1"
        placeholder="Введите количество вакансий"
        data-qa="vacancies-number-input"
      />
      {error && <div className={styles.error}>{error}</div>}
    </div>
  );
};

export default VacanciesNumber; 