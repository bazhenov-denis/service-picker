import { FC } from "react";
import { Card } from "@hh.ru/magritte-ui-card";
import { Text } from "@hh.ru/magritte-ui-typography";
import styles from "./OfferDisplay.module.css";
import type { OfferItemDto } from "../../types/service";

interface OfferDisplayProps {
  offer: {
    items: (OfferItemDto & { label?: string })[];
  };
}

const formatFieldName = (field: string): string => {
  const fieldNames: Record<string, string> = {
    period: "Период",
    price: "Цена",
    civCount: "Количество контактов",
    vacancyCount: "Количество вакансий",
    apiLimitedCount: "Лимит API",
    region: "Регион",
    profroleGroup: "Группа профессий"
  };
  return fieldNames[field] || field;
};

const formatFieldValue = (field: string, value: string | number | null): string => {
  if (value === null) return "";
  
  if (field === "price") {
    return `${parseFloat(value.toString()).toLocaleString('ru-RU')} ₽`;
  }
  if (field === "period") {
    return `${value} дней`;
  }
  return value.toString();
};

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
          <div className={styles.headerRow}>
            <Text typography="subtitle-1-semibold">
              {item.label ? `${item.label}: ${item.title}` : item.title}
            </Text>
          </div>
          <div className={styles.itemDetails}>
            {Object.entries(item).map(([key, value]) => {
              if (key === "type" || key === "title" || key === "label" || value === null) return null;
              
              return (
                <div key={key} className={styles.detailRow}>
                  <Text typography="subtitle-3-semibold">{formatFieldName(key)}:</Text>
                  <Text typography="label-3-regular">{formatFieldValue(key, value)}</Text>
                </div>
              );
            })}
          </div>
        </Card>
      ))}
    </div>
  );
};

export default OfferDisplay;
