import React from 'react';
import styles from './VacanciesNumber.module.css';

interface VacanciesNumberProps {
  onNumberChange: (number: number) => void;
}

export const VacanciesNumber: React.FC<VacanciesNumberProps> = ({ onNumberChange }) => {
  const [number, setNumber] = React.useState<number>(0);
  const [error, setError] = React.useState<string>('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value);
    if (value > 0) {
      setNumber(value);
      setError('');
      onNumberChange(value);
    } else {
      setError('Число должно быть больше 0');
    }
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
        data-qa="vacancies-number-input"
      />
      {error && <div className={styles.error}>{error}</div>}
    </div>
  );
};

export default VacanciesNumber; 