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
        const transformedData = transformRegions(mockRegions);
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