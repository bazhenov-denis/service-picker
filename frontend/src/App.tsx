import { FC } from "react";
import Header from "../src/pages/ServicePickerPage/components/Header/Header";
import HierarchicalSelector from "./pages/ServicePickerPage/components/HierarchicalSelector/HierarchicalSelector";
import VacanciesNumber from "./pages/ServicePickerPage/components/VacanciesNumber/VacanciesNumber";
import ServicePickerButton from "./pages/ServicePickerPage/components/ServicePickerButton/ServicePickerButton";
import OfferDisplay from "./pages/ServicePickerPage/components/OfferDisplay/OfferDisplay";
import { useServicePicker } from "./pages/ServicePickerPage/hooks/useServicePicker";
import { useRegions } from "./pages/ServicePickerPage/hooks/useRegions";
import { useProfessions } from "./pages/ServicePickerPage/hooks/useProfessions";
import styles from "./App.module.css";

const App: FC = () => {
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
    isLoading,
  } = useServicePicker();

  const {
    collection: regionsCollection,
    loading: regionsLoading,
    error: regionsError,
  } = useRegions();
  const {
    collection: professionsCollection,
    loading: professionsLoading,
    error: professionsError,
  } = useProfessions();

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
};

export default App;
