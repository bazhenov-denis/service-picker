import { TreeModel } from '@hh.ru/magritte-ui-tree-selector/collection/types';

interface Profession {
  id: string;
  name: string;
  items?: Profession[];
}

interface CustomTreeModel extends TreeModel {
  items?: CustomTreeModel[];
}

interface ProfessionResponse {
  categories: Array<{
    id: string;
    name: string;
    roles: Array<{
      id: string;
      name: string;
    }>;
  }>;
}

export const transformApiResponse = (response: ProfessionResponse): CustomTreeModel[] => {
  const professions = response.categories.map(category => ({
    id: category.id,
    name: category.name,
    items: category.roles
  }));
  return transformProfessionsToTreeModel(professions);
};

export const transformProfessionsToTreeModel = (professions: Profession[]): CustomTreeModel[] => {
  return professions.map(category => ({
    id: `category_${category.id}`,
    text: category.name,
    items: category.items?.map(role => ({
      id: `role_${role.id}`,
      text: role.name
    }))
  }));
}; 