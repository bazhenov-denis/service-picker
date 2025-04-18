// src/components/RegionSelector/index.tsx
import React, { useState } from 'react';
import { TreeSelector } from '@hh.ru/magritte-ui';

// Пример mock данных для дерева регионов
const mockRegions = [
  {
    id: '1',
    label: 'Россия',
    children: [
      { id: '2', label: 'Москва' },
      { id: '3', label: 'Санкт-Петербург' },
    ],
  },
  {
    id: '4',
    label: 'Казахстан',
    children: [
      { id: '5', label: 'Астана' },
      { id: '6', label: 'Алматы' },
    ],
  },
];

const RegionSelector: React.FC = () => {
  // Состояние для выбранных значений
  const [selectedRegions, setSelectedRegions] = useState<string[]>([]);

  // Обработчик изменения выбора
  const handleRegionChange = (
    allSelected: string[],
    id: string,
    isSelected: boolean
  ) => {
    console.log('Выбранные регионы:', allSelected);
    console.log(`Элемент с ID ${id} был ${isSelected ? 'выбран' : 'отменён'}`);
    setSelectedRegions(allSelected);
  };

  return (
    <div>
      <h2>Выберите регион:</h2>
      <TreeSelector
        collection={mockRegions} // Данные для дерева
        value={selectedRegions} // Текущие выбранные значения
        onChange={handleRegionChange} // Колбек на изменение выбора
        singleChoice={false} // Разрешить множественный выбор
      />
    </div>
  );
};

export default RegionSelector;