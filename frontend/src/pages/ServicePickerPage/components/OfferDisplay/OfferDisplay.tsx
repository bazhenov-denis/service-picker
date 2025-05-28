import { FC } from "react";
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
      <h3>Предложенные услуги:</h3>
      {offer.items.map((item, index) => (
        <div key={index} className={styles.offerItem}>
          <h4>{item.title}</h4>
          <p>Тип: {item.type}</p>
          <p>Период: {item.period} дней</p>
          <p>Цена: {parseFloat(item.price).toLocaleString('ru-RU')} ₽</p>
          {item.civCount && <p>Количество контактов: {item.civCount}</p>}
          {item.vacancyCount && <p>Количество вакансий: {item.vacancyCount}</p>}
          {item.apiLimitedCount && <p>Лимит API: {item.apiLimitedCount}</p>}
        </div>
      ))}
    </div>
  );
};

export default OfferDisplay;
