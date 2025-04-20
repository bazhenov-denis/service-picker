import React, { useState, useMemo, useCallback, useRef } from "react";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { TreeSelector } from "@hh.ru/magritte-ui-tree-selector";
import { useRegions } from '../../hooks/useRegions'; // Импортируем хук
import { TreeModel } from '@hh.ru/magritte-ui-tree-selector/collection/types';
import type { ListControls } from "@hh.ru/magritte-ui-tree-selector";
import styles from './RegionSelector.module.css';
import { useDelayedRender } from '../../hooks/useDelayedRender';

interface CustomTreeModel extends TreeModel {
  items?: CustomTreeModel[];
}

interface RegionSelectorProps {
  onRegionsChange: (regions: string[]) => void;
}

const RegionSelector: React.FC<RegionSelectorProps> = ({ onRegionsChange }) => {
  const [selectedRegions, setSelectedRegions] = useState<string[]>([]);
  const controlsRef = useRef<ListControls>(null);
  
  const { regions, loading, error } = useRegions();
  const isVisible = useDelayedRender(300);

  // Используем данные напрямую, без дополнительного преобразования
  const treeData = useMemo(() => {
    if (!regions?.length) return [];
    console.log('Tree data:', regions);
    return regions;
  }, [regions]);

  // Создаем коллекцию для TreeSelector
  const collection = useMemo(() => {
    const coll = new TreeCollection();
    
    const addModelRecursively = (model: CustomTreeModel, parentId?: string) => {
      coll.addModel(model, parentId);
      if (model.items) {
        model.items.forEach(item => addModelRecursively(item, model.id));
      }
    };

    treeData.forEach(region => addModelRecursively(region));
    return coll;
  }, [treeData]);

  const handleRegionChange = useCallback((allSelected: string[]) => {
    setSelectedRegions(allSelected);
    onRegionsChange(allSelected);
  }, [onRegionsChange]);

  const getSelectAllParentTrl = useCallback((id: string) => {
    return `Выбрать все`;
  }, []);

  if (!isVisible) {
    return <div className={styles.loadingContainer}>
      <div>Загрузка регионов...</div>
    </div>;
  }

  if (loading) {
    return (
      <div className={styles.loadingContainer}>
        <div>Загрузка регионов...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.errorContainer}>
        <div>
          <p>Ошибка при загрузке регионов</p>
          <p className={styles.errorMessage}>Пожалуйста, проверьте подключение к серверу</p>
        </div>
      </div>
    );
  }
  
  return (
    <div className={styles.regionSelector}>
      <h2>Выберите регион:</h2>
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
          value={selectedRegions}
          onChange={handleRegionChange}
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

export default RegionSelector;