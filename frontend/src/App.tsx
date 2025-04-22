import React from 'react';
import Header from './components/Header/Header';
import RegionSelector from './components/RegionSelector/RegionSelector';
import ProfessionSelector from './components/ProfessionSelector/ProfessionSelector';
import VacanciesNumber from './components/VacanciesNumber/VacanciesNumber';
import ServicePickerButton from './components/ServicePickerButton/ServicePickerButton';
import OfferDisplay from './components/OfferDisplay/OfferDisplay';
import { useServicePicker } from './hooks/useServicePicker';
import styles from './App.module.css';

function App() {
  const {
    selectedRegions,
    setSelectedRegions,
    selectedProfessions,
    setSelectedProfessions,
    vacanciesNumber,
    setVacanciesNumber,
    handleSendData,
    offer,
    error,
    isLoading
  } = useServicePicker();

  return (
    <div className={styles.app}>
      <Header />
      <div className={styles.container}>
        <RegionSelector onRegionsChange={setSelectedRegions} />
        <ProfessionSelector onProfessionsChange={setSelectedProfessions} />
        <VacanciesNumber onNumberChange={setVacanciesNumber} />
        <ServicePickerButton
          selectedRegions={selectedRegions}
          selectedProfessions={selectedProfessions}
          vacanciesNumber={vacanciesNumber}
          onSendData={handleSendData}
          isLoading={isLoading}
          error={error}
        />
        {offer && <OfferDisplay offer={offer} />}
      </div>
    </div>
  );
}

export default App;