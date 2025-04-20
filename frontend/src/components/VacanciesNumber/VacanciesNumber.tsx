import React, { useState, ChangeEvent } from 'react';
import styles from './VacanciesNumber.module.css';

const VacanciesNumber: React.FC = () => {
  const [value, setValue] = useState<string>('');
  const [error, setError] = useState<string>('');

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const inputValue = e.target.value;
    
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
      <input
        type="text"
        value={value}
        onChange={handleChange}
        className={styles.input}
        placeholder="Введите количество вакансий"
      />
      {error && <div className={styles.error}>{error}</div>}
    </div>
  );
};

export default VacanciesNumber; 