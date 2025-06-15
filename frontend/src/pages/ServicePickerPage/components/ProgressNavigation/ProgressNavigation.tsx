import React from "react";
import { ProgressBar } from "@hh.ru/magritte-ui-progress-bar";
import { Button } from "@hh.ru/magritte-ui-button";
import "@hh.ru/magritte-ui-progress-bar/index.css";
import "@hh.ru/magritte-ui-button/index.css";
import styles from "./ProgressNavigation.module.css";

interface ProgressNavigationProps {
  currentSegment: number;
  totalSegments: number;
  onSegmentChange: (segment: number) => void;
  canProceed: boolean;
  isLastSegment: boolean;
}

export const ProgressNavigation: React.FC<ProgressNavigationProps> = ({
  currentSegment,
  totalSegments,
  onSegmentChange,
  canProceed,
  isLastSegment,
}) => {
  const handleBack = () => {
    if (currentSegment > 0) {
      onSegmentChange(currentSegment - 1);
    }
  };

  const handleNext = () => {
    if (currentSegment < totalSegments - 1 && canProceed) {
      onSegmentChange(currentSegment + 1);
    }
  };

  return (
    <div className={styles.container}>
      <ProgressBar
        progress={currentSegment + 1}
        segments={totalSegments}
        style="accent"
        aria-label="Прогресс заполнения вопросов"
        data-qa="progress-navigation-bar"
      />

      <div className={styles.navigationButtons}>
        <Button
          mode="secondary"
          size="medium"
          disabled={currentSegment === 0}
          onClick={handleBack}
          data-qa="progress-back-button"
        >
          <span className={styles.buttonText}>Назад</span>
        </Button>

        <Button
          mode="primary"
          size="medium"
          disabled={!canProceed || isLastSegment}
          onClick={handleNext}
          data-qa="progress-next-button"
        >
          <span className={styles.buttonText}>Продолжить</span>
        </Button>
      </div>
    </div>
  );
};

export default ProgressNavigation;
