import React, { useState } from "react";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { TreeSelector } from "@hh.ru/magritte-ui-tree-selector"; // Исправленный импорт

interface Region {
  id: string;
  label: string;
  children?: Region[];
}

interface TreeModel {
  id: string;
  text: string;
  items?: TreeModel[];
}

const mockRegions: Region[] = [
  {
    id: "1",
    label: "Россия",
    children: [
      { id: "2", label: "Москва" },
      { id: "3", label: "Санкт-Петербург" },
    ],
  },
  {
    id: "4",
    label: "Казахстан",
    children: [
      { id: "5", label: "Астана" },
      { id: "6", label: "Алматы" },
    ],
  },
];

const transformToTreeModel = (regions: Region[]): TreeModel[] => {
  return regions.map((region) => ({
    id: region.id,
    text: region.label,
    items: region.children ? transformToTreeModel(region.children) : undefined,
  }));
};

const RegionSelector: React.FC = () => {
  const [selectedRegions, setSelectedRegions] = useState<string[]>([]);
  const collection = new TreeCollection();
  const treeData = transformToTreeModel(mockRegions);

  treeData.forEach((region) => {
    collection.addModel(region);
  });

  const handleRegionChange = (
    allSelected: string[],
    id: string,
    isSelected: boolean
  ) => {
    setSelectedRegions(allSelected);
  };

  return (
    <div>
      <h2>Выберите регион:</h2>
      <TreeSelector
        collection={collection}
        value={selectedRegions}
        onChange={handleRegionChange}
        singleChoice={false}
        getSelectAllParentTrl={() => "Выбрать все"}
      >
        {({ renderTreeSelector }) => renderTreeSelector()}
      </TreeSelector>
    </div>
  );
};

export default RegionSelector;