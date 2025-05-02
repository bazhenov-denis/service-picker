import { useMemo, useState, useEffect } from "react";
import { professions } from "../__mocks__/professions";
import { transformApiResponse } from "../utils/professionsTransformer";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { TreeModel } from "@hh.ru/magritte-ui-tree-selector/collection/types";
import { fetchProfessions } from "../api/httpClient";

interface CustomTreeModel extends TreeModel {
  items?: CustomTreeModel[];
}

interface ProfessionCategory {
  id: string;
  name: string;
  roles: Array<{
    id: string;
    name: string;
  }>;
}

interface ProfessionResponse {
  categories: ProfessionCategory[];
}

export const useProfessions = () => {
  const [professionData, setProfessionData] = useState<any[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadProfessions = async () => {
      try {
        setLoading(true);
        const response = await fetchProfessions();
        const transformedData = transformApiResponse(response as any);
        // const transformedData = transformProfessionsToTreeModel(professions);
        setProfessionData(transformedData);
      } catch (err) {
        console.error("Ошибка при загрузке профессий:", err);
        setError("Не удалось загрузить профессии");
      } finally {
        setLoading(false);
      }
    };

    loadProfessions();
  }, []);

  const collection = useMemo(() => {
    const coll = new TreeCollection();

    const addModelRecursively = (model: CustomTreeModel, parentId?: string) => {
      coll.addModel(model, parentId);
      if (model.items) {
        model.items.forEach((item) => addModelRecursively(item, model.id));
      }
    };

    professionData.forEach((profession) => addModelRecursively(profession));
    return coll;
  }, [professionData]);

  const getOriginalId = (id: string) => {
    return id.replace(/^(category_|role_)/, "");
  };

  return {
    collection,
    getOriginalId,
    loading,
    error,
  };
};
