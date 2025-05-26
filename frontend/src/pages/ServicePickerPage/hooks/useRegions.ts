// hooks/useRegions.ts

import { useState, useEffect, useMemo } from "react";
import { transformRegions } from "../utils/transformRegions";
import { fetchRegions } from "../client/httpClient";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { TreeModel } from "@hh.ru/magritte-ui-tree-selector/collection/types";

export interface CustomTreeModel extends TreeModel {
  items?: CustomTreeModel[];
}

/**
 * Хук для загрузки и преобразования данных регионов.
 * @returns Объект, содержащий данные, состояние загрузки и ошибки.
 */
export const useRegions = () => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [regions, setRegions] = useState<CustomTreeModel[]>([]);

  // Создаем коллекцию для TreeSelector
  const collection = useMemo(() => {
    const coll = new TreeCollection();

    const addModelRecursively = (model: CustomTreeModel, parentId?: string) => {
      coll.addModel(model, parentId);
      if (model.items) {
        model.items.forEach((item) => addModelRecursively(item, model.id));
      }
    };

    regions.forEach((region) => addModelRecursively(region));
    return coll;
  }, [regions]);

  useEffect(() => {
    const loadRegions = async () => {
      try {
        setLoading(true);
        const data = await fetchRegions();
        const transformedData = transformRegions(data);
        // const transformedData = transformRegions(mockRegions);
        setRegions(transformedData);
      } catch (err) {
        console.error("Ошибка при загрузке регионов:", err);
        setError("Не удалось загрузить регионы");
      } finally {
        setLoading(false);
      }
    };

    loadRegions();
  }, []);

  return { regions, collection, loading, error };
};

export {};
