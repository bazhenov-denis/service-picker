import { useMemo } from 'react';
import { professions } from '../mocks/professions';
import { transformProfessionsToTreeModel } from '../utils/professionsTransformer';
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { TreeModel } from '@hh.ru/magritte-ui-tree-selector/collection/types';

interface CustomTreeModel extends TreeModel {
  items?: CustomTreeModel[];
}

export const useProfessions = () => {
  const treeData = useMemo(() => {
    return transformProfessionsToTreeModel(professions);
  }, []);

  const collection = useMemo(() => {
    const coll = new TreeCollection();
    
    const addModelRecursively = (model: CustomTreeModel, parentId?: string) => {
      coll.addModel(model, parentId);
      if (model.items) {
        model.items.forEach(item => addModelRecursively(item, model.id));
      }
    };

    treeData.forEach(profession => addModelRecursively(profession));
    return coll;
  }, [treeData]);

  const getOriginalId = (id: string) => {
    return id.replace(/^(category_|role_)/, '');
  };

  return {
    collection,
    getOriginalId
  };
}; 