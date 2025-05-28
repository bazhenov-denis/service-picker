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
          <Text typography="subtitle-2-semibold">{item.title}</Text>
          <div className={styles.itemDetails}>
            <Text typography="label-3-regular">Тип: {item.type}</Text>
            <Text typography="label-3-regular">Период: {item.period} дней</Text>
            <Text typography="label-3-regular">
              Цена: {parseFloat(item.price).toLocaleString('ru-RU')} ₽
            </Text>
            {item.civCount && (
              <Text typography="label-3-regular">
                Количество контактов: {item.civCount}
              </Text>
            )}
            {item.vacancyCount && (
              <Text typography="label-3-regular">
                Количество вакансий: {item.vacancyCount}
              </Text>
            )}
            {item.apiLimitedCount && (
              <Text typography="label-3-regular">
                Лимит API: {item.apiLimitedCount}
              </Text>
            )}
          </div>
        </Card>
      ))}
    </div>
  );
};

export default OfferDisplay;
