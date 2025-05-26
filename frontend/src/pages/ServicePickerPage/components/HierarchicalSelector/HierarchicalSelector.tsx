import { FC } from "react";
import { TreeSelector } from "@hh.ru/magritte-ui-tree-selector";
import type { ListControls } from "@hh.ru/magritte-ui-tree-selector";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { useDelayedRender } from "../../hooks/useDelayedRender";
import styles from "./HierarchicalSelector.module.css";

interface HierarchicalSelectorProps {
  title: string;
  collection: TreeCollection;
  selectedItems: string[];
  onItemsChange: (items: string[]) => void;
  loading: boolean;
  error: any;
  loadingDelay?: number;
  placeholder?: string;
  dataQa?: string;
}

export const HierarchicalSelector: FC<HierarchicalSelectorProps> = ({
  title,
  collection,
  selectedItems,
  onItemsChange,
  loading,
  error,
  loadingDelay = 600,
  placeholder = "Выберите значение",
  dataQa,
}) => {
  const isVisible = useDelayedRender(loadingDelay);

  const getSelectAllParentTrl = () => "Выбрать все";

  if (!isVisible || loading) {
    return (
      <div className={styles.loadingContainer}>
        <div>Загрузка...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.errorContainer}>
        <div>
          <p>Ошибка при загрузке данных</p>
          <p className={styles.errorMessage}>
            Пожалуйста, проверьте подключение к серверу
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.selectorContainer}>
      <h2 className={styles.title}>{title}</h2>
      <TreeSelector
        collapseToParentId
        collection={collection}
        value={selectedItems}
        onChange={onItemsChange}
        getSelectAllParentTrl={getSelectAllParentTrl}
        data-qa={dataQa}
        onMobileNavigationChange={(currentId) => {
          console.log('Current navigation ID:', currentId);
        }}
      >
        {({ renderTreeSelector, renderInput }) => (
          <div className={styles.treeWrapper}>
            <div className={styles.inputContainer}>{renderInput()}</div>
            <div className={styles.treeContainer}>{renderTreeSelector()}</div>
          </div>
        )}
      </TreeSelector>
    </div>
  );
};

export default HierarchicalSelector;
