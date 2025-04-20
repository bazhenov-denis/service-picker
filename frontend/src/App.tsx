import React from 'react';
import Header from './components/Header/Header';
import RegionSelector from './components/RegionSelector/RegionSelector';
import ProfessionSelector from './components/ProfessionSelector/ProfessionSelector';
import VacanciesNumber from './components/VacanciesNumber/VacanciesNumber';
import styles from './App.module.css';

function App() {
  return (
    <div className={styles.app}>
      <Header />
      <div className={styles.container}>
        <RegionSelector />
        <ProfessionSelector />
        <VacanciesNumber />
      </div>
    </div>
  );
}

export default App;