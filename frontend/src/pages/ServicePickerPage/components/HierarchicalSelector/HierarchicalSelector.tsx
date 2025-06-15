import { FC, useRef, useEffect, useCallback, useMemo, useState } from "react";
import { TreeSelector } from "@hh.ru/magritte-ui-tree-selector";
import type { ListControls } from "@hh.ru/magritte-ui-tree-selector";
import TreeCollection from "@hh.ru/magritte-ui-tree-selector/collection/treeCollection";
import { useDelayedRender } from "../../hooks/useDelayedRender";
import { BackIcon } from "./BackIcon";
import styles from "./HierarchicalSelector.module.css";
import "@hh.ru/magritte-ui-checkbox-radio/index.css";
import "@hh.ru/magritte-ui-tree-selector/index.css";
import { TreeModel } from "@hh.ru/magritte-ui-tree-selector/collection/types";

// Простой компонент скелетона с анимацией
const Skeleton: FC<{ width: string | number; height: string | number }> = ({
  width,
  height,
}) => <div className={styles.skeleton} style={{ width, height }} />;

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

const HierarchicalSelector: FC<HierarchicalSelectorProps> = ({
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
  const controlsRef = useRef<ListControls>(null);

  const selectedIds = useMemo(() => {
    return selectedItems.flatMap((path) => path.split("."));
  }, [selectedItems]);

  const [localSelectedIds, setLocalSelectedIds] =
    useState<string[]>(selectedIds);

  useEffect(() => {
    setLocalSelectedIds(selectedIds);
  }, []);

  const handleItemsChange = useCallback(
    (newSelectedIds: string[]) => {
      setLocalSelectedIds(newSelectedIds);

      if (!newSelectedIds.length) {
        onItemsChange([]);
        return;
      }

      const newSelectedPaths = newSelectedIds.map((id) => {
        const path: string[] = [];
        let currentId: string | undefined = id;

        while (currentId) {
          const node: TreeModel | undefined = collection.getModel(currentId);
          if (!node) break;

          path.unshift(currentId);
          const parent: TreeModel | undefined = collection.getParent(currentId);
          currentId = parent?.id;
        }

        return path.join(".");
      });

      onItemsChange(newSelectedPaths);
    },
    [collection, onItemsChange],
  );

  const handleBackClick = () => {
    controlsRef.current?.back();
  };

  useEffect(() => {
    document.body.classList.add("magritte-old-layout");
    return () => {
      document.body.classList.remove("magritte-old-layout");
    };
  }, []);

  const getSelectAllParentTrl = () => "Выбрать все";

  if (!isVisible || loading) {
    return (
      <div className={styles.loadingContainer}>
        <div style={{ marginBottom: 12 }}>
          <Skeleton width={180} height={24} />
        </div>
        <div style={{ marginBottom: 8 }}>
          <Skeleton width="100%" height={36} />
        </div>
        <Skeleton width="100%" height="calc(100% - 80px)" />
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
        ref={controlsRef}
        collapseToParentId
        collection={collection}
        value={localSelectedIds}
        onChange={handleItemsChange}
        getSelectAllParentTrl={getSelectAllParentTrl}
        data-qa={dataQa}
        onMobileNavigationChange={(currentId) => {
          console.log("Current navigation ID:", currentId);
        }}
      >
        {({ renderTreeSelector, renderInput }) => (
          <div className={styles.treeWrapper}>
            <div className={styles.inputContainer}>
              <button
                className={styles.backButton}
                onClick={handleBackClick}
                data-qa={`${dataQa}-back-button`}
                aria-label="Назад"
              >
                <BackIcon />
              </button>
              {renderInput()}
            </div>
            <div className={styles.treeContainer}>{renderTreeSelector()}</div>
          </div>
        )}
      </TreeSelector>
    </div>
  );
};

export default HierarchicalSelector;
