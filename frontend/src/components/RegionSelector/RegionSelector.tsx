import React, { useState, useMemo, useCallback, useRef } from "react";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { TreeSelector } from "@hh.ru/magritte-ui-tree-selector";
import { useRegions } from '../../hooks/useRegions'; // Импортируем хук
import { TreeModel } from '@hh.ru/magritte-ui-tree-selector/collection/types';
import type { ListControls } from "@hh.ru/magritte-ui-tree-selector";

interface CustomTreeModel extends TreeModel {
  items?: CustomTreeModel[];
}

const RegionSelector: React.FC = () => {
  const [selectedRegions, setSelectedRegions] = useState<string[]>([]);
  const controlsRef = useRef<ListControls>(null);
  
  const { regions, loading, error } = useRegions();

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
  }, []);

  const getSelectAllParentTrl = useCallback((id: string) => {
    return `Выбрать все (${id})`;
  }, []);

  if (loading) {
    return (
      <div style={{ 
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center', 
        height: '200px' 
      }}>
        <div>Загрузка регионов...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ 
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center', 
        height: '200px',
        color: 'red',
        textAlign: 'center',
        padding: '20px'
      }}>
        <div>
          <p>Ошибка при загрузке регионов</p>
          <p style={{ fontSize: '14px', color: '#666' }}>Пожалуйста, проверьте подключение к серверу</p>
        </div>
      </div>
    );
  }
  
  return (
    <div style={{ position: "relative", height: "400px", width: "100%" }}>
      <h2>Выберите регион:</h2>
      <div style={{ 
        border: "1px solid #e0e0e0",
        borderRadius: "4px",
        backgroundColor: "#fff",
        padding: "16px",
        height: "calc(100% - 60px)"
      }}>
        <div style={{
          display: 'flex',
          gap: '8px',
          marginBottom: '12px'
        }}>
          <button
            onClick={() => controlsRef.current?.back()}
            style={{
              padding: '8px 16px',
              border: '1px solid #e0e0e0',
              borderRadius: '4px',
              backgroundColor: '#fff',
              cursor: 'pointer'
            }}
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
            <div style={{ 
              display: 'flex',
              flexDirection: 'column',
              gap: '12px',
              height: '100%'
            }}>
              <div style={{ flexShrink: 0 }}>
                {renderInput()}
              </div>
              <div style={{ 
                flex: 1,
                overflow: "auto",
                minHeight: 0
              }}>
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