// hooks/useRegions.ts

import { useState, useEffect } from 'react';
import { transformRegions } from '../utils/transformRegions';
import { mockRegions } from '../api/mocks/regions';

/**
 * Хук для загрузки и преобразования данных регионов.
 * @returns Объект, содержащий данные, состояние загрузки и ошибки.
 */
export const useRegions = () => {
  const [regions, setRegions] = useState<any[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadRegions = async () => {
      try {
        setLoading(true);
        console.log('Original mock data:', mockRegions);
        // Преобразуем данные в нужный формат
        const transformedData = mockRegions.map(region => ({
          id: region.area_id,
          label: region.name,
          items: region.areas ? region.areas.map(area => ({
            id: area.area_id,
            label: area.name,
            items: area.areas ? area.areas.map(subArea => ({
              id: subArea.area_id,
              label: subArea.name
            })) : undefined
          })) : undefined
        }));
        console.log('Transformed regions data:', transformedData);
        setRegions(transformedData);
      } catch (err) {
        console.error('Ошибка при загрузке регионов:', err);
        setError('Не удалось загрузить регионы');
      } finally {
        setLoading(false);
      }
    };

    loadRegions();
  }, []);

  return { regions, loading, error };
};

export {};