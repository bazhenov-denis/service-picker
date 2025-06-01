import { FC } from "react";
import { Card } from "@hh.ru/magritte-ui-card";
import { Text } from "@hh.ru/magritte-ui-typography";
import styles from "./OfferDisplay.module.css";
import type { OfferItemDto } from "../../types/service";

interface OfferDisplayProps {
  offer: {
    items: OfferItemDto[];
  };
}

export const OfferDisplay: FC<OfferDisplayProps> = ({ offer }) => {
  if (!offer.items || offer.items.length === 0) {
    return null;
  }

  return (
    <div className={styles.offerContainer}>
      <Text typography="subtitle-1-semibold" className={styles.title}>
        Предложенные услуги:
      </Text>
      {offer.items.map((item, index) => (
        <Card
          key={index}
          className={styles.offerItem}
          padding={16}
          borderRadius={8}
          style="primary"
          shadow="level-2"
        >
          <Text typography="subtitle-1-semibold">{item.title}</Text>
          <div className={styles.itemDetails}>
            <div className={styles.detailRow}>
              <Text typography="subtitle-3-semibold">Тип:</Text>
              <Text typography="label-3-regular">{item.type}</Text>
            </div>
            <div className={styles.detailRow}>
              <Text typography="subtitle-3-semibold">Период:</Text>
              <Text typography="label-3-regular">{item.period} дней</Text>
            </div>
            <div className={styles.detailRow}>
              <Text typography="subtitle-3-semibold">Цена:</Text>
              <Text typography="label-3-regular">
                {parseFloat(item.price).toLocaleString('ru-RU')} ₽
              </Text>
            </div>
            {item.civCount && (
              <div className={styles.detailRow}>
                <Text typography="subtitle-3-semibold">Количество контактов:</Text>
                <Text typography="label-3-regular">{item.civCount}</Text>
              </div>
            )}
            {item.vacancyCount && (
              <div className={styles.detailRow}>
                <Text typography="subtitle-3-semibold">Количество вакансий:</Text>
                <Text typography="label-3-regular">{item.vacancyCount}</Text>
              </div>
            )}
            {item.apiLimitedCount && (
              <div className={styles.detailRow}>
                <Text typography="subtitle-3-semibold">Лимит API:</Text>
                <Text typography="label-3-regular">{item.apiLimitedCount}</Text>
              </div>
            )}
          </div>
        </Card>
      ))}
    </div>
  );
};

export default OfferDisplay;
