// utils/transformRegions.ts

import { ModelData } from "@hh.ru/magritte-ui-tree-selector/collection/types"; // Импортируем тип ModelData

/**
 * Преобразует данные из API в формат ModelData.
 * @param data - Массив данных из API.
 * @returns Массив объектов типа ModelData.
 */
export function transformRegions(data: any[]): ModelData[] {
  return data.map((item) => {
    const model: ModelData = {
      id: item.id,
      text: item.name, // Название региона
    };

    // Если есть вложенные области (areas), рекурсивно преобразуем их в items
    if (item.areas && Array.isArray(item.areas)) {
      model.items = transformRegions(item.areas);
    }

    return model;
  });
}
