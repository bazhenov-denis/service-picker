import { TreeModel } from "@hh.ru/magritte-ui-tree-selector/collection/types";
import { title } from "process";

interface Profession {
  id: string;
  name: string;
  items?: Profession[];
}

interface CustomTreeModel extends TreeModel {
  items?: CustomTreeModel[];
}

interface ProfessionResponse {
  title: string; // для мок данных
  categories: Array<{
    id: string;
    name: string;
    roles: Array<{
      id: string;
      name: string;
    }>;
  }>;
}

export const transformApiResponse = (
  response: ProfessionResponse
): { collection: CustomTreeModel[]; title: string } => {
  const professions = response.categories.map((category) => ({
    id: category.id,
    name: category.name,
    items: category.roles,
  }));

  const collection = transformProfessionsToTreeModel(professions);
  return {
    collection,
    title: response.title,
  };
};

export const transformProfessionsToTreeModel = (
  professions: Profession[]
): CustomTreeModel[] => {
  return professions.map((category) => ({
    id: `category_${category.id}`,
    text: category.name,
    items: category.items?.map((role) => ({
      id: `role_${role.id}`,
      text: role.name,
    })),
  }));
};
