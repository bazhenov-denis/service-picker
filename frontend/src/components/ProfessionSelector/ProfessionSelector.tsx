import React, { useState, useCallback, useRef } from "react";
import { TreeSelector } from "@hh.ru/magritte-ui-tree-selector";
import type { ListControls } from "@hh.ru/magritte-ui-tree-selector";
import { useProfessions } from '../../hooks/useProfessions';
import styles from './ProfessionSelector.module.css';
import { useDelayedRender } from '../../hooks/useDelayedRender';

const ProfessionSelector: React.FC = () => {
  const [selectedProfessions, setSelectedProfessions] = useState<string[]>([]);
  const controlsRef = useRef<ListControls>(null);
  const { collection, getOriginalId, loading, error } = useProfessions();
  const isVisible = useDelayedRender(200); // Немного большая задержка, чем у RegionSelector

  const handleProfessionChange = useCallback((allSelected: string[]) => {
    setSelectedProfessions(allSelected.map(getOriginalId));
  }, [getOriginalId]);

  const getSelectAllParentTrl = useCallback((id: string) => {
    return `Выбрать все (${getOriginalId(id)})`;
  }, [getOriginalId]);

  if (!isVisible || loading) {
    return (
      <div className={styles.loadingContainer}>
        <div>Загрузка профессий...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.errorContainer}>
        <div>
          <p>Ошибка при загрузке профессий</p>
          <p className={styles.errorMessage}>Пожалуйста, проверьте подключение к серверу</p>
        </div>
      </div>
    );
  }
  
  return (
    <div className={styles.professionSelector}>
      <h2>Выберите профессию:</h2>
      <div className={styles.selectorContainer}>
        <div className={styles.controls}>
          <button
            onClick={() => controlsRef.current?.back()}
            className={styles.backButton}
          >
            ← Назад
          </button>
        </div>
        <TreeSelector
          collapseToParentId
          collection={collection}
          value={selectedProfessions.map(id => `role_${id}`)}
          onChange={handleProfessionChange}
          ref={controlsRef}
          getSelectAllParentTrl={getSelectAllParentTrl}
        >
          {({ renderTreeSelector, renderInput }) => (
            <div className={styles.treeWrapper}>
              <div className={styles.inputContainer}>
                {renderInput()}
              </div>
              <div className={styles.treeContainer}>
                {renderTreeSelector()}
              </div>
            </div>
          )}
        </TreeSelector>
      </div>
    </div>
  );
};

export default ProfessionSelector; 