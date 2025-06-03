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

interface HierarchicalSelectorProps {
  title: string;
  collection: TreeCollection;
  selectedItems: string[]; // массив путей вида ["113.1620.1621"]
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

  // Локальное состояние для выбранных ID
  const [localSelectedIds, setLocalSelectedIds] = useState<string[]>([]);

  /**
   * Преобразование путей вида "113.1620.1621" в массив id терминальных нод ["1621"]
   */
  const selectedIds = useMemo(() => {
    return selectedItems.map((path) => {
      const parts = path.split(".");
      return parts[parts.length - 1];
    });
  }, [selectedItems]);

  // Синхронизируем локальное состояние с пропсами
  useEffect(() => {
    setLocalSelectedIds(selectedIds);
  }, [selectedIds]);

  /**
   * Преобразование id обратно в путь вида "113.1620.1621"
   */
  const handleItemsChange = useCallback(
    (newSelectedIds: string[]) => {
      setLocalSelectedIds(newSelectedIds); // Обновляем локальное состояние

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
    [collection, onItemsChange]
  );

  /**
   * Назад для мобильного режима
   */
  const handleBackClick = () => {
    controlsRef.current?.back();
  };

  /**
   * Применяем временный класс для совместимости
   */
  useEffect(() => {
    document.body.classList.add("magritte-old-layout");
    return () => {
      document.body.classList.remove("magritte-old-layout");
    };
  }, []);

  /**
   * Локализация для кнопки "Выбрать все"
   */
  const getSelectAllParentTrl = () => "Выбрать все";

  /**
   * Скелетон загрузки
   */
  if (!isVisible || loading) {
    return (
      <div className={styles.loadingContainer}>
        <div>Загрузка...</div>
      </div>
    );
  }

  /**
   * Ошибка загрузки данных
   */
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

  /**
   * Основной рендер компонента
   */
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
