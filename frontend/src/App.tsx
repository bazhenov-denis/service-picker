import React from 'react';
import Header from './components/Header/Header';
import HierarchicalSelector from './components/HierarchicalSelector/HierarchicalSelector';
import VacanciesNumber from './components/VacanciesNumber/VacanciesNumber';
import ServicePickerButton from './components/ServicePickerButton/ServicePickerButton';
import OfferDisplay from './components/OfferDisplay/OfferDisplay';
import { useServicePicker } from './hooks/useServicePicker';
import { useRegions } from './hooks/useRegions';
import { useProfessions } from './hooks/useProfessions';
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

  const { collection: regionsCollection, loading: regionsLoading, error: regionsError } = useRegions();
  const { collection: professionsCollection, loading: professionsLoading, error: professionsError } = useProfessions();

  return (
    <div className={styles.app}>
      <Header />
      <div className={styles.container}>
        <HierarchicalSelector
          title="Выберите регион"
          collection={regionsCollection}
          selectedItems={selectedRegions}
          onItemsChange={setSelectedRegions}
          loading={regionsLoading}
          error={regionsError}
          dataQa="region-selector"
        />
        <HierarchicalSelector
          title="Выберите профессию"
          collection={professionsCollection}
          selectedItems={selectedProfessions}
          onItemsChange={setSelectedProfessions}
          loading={professionsLoading}
          error={professionsError}
          dataQa="profession-selector"
        />
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