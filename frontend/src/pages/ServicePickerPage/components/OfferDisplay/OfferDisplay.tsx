import React from "react";
import styles from "./OfferDisplay.module.css";

interface VacancyOffer {
  id: number;
  areaId: number;
  professionId: number;
  packageVolume: number;
  vacancyType: string;
  publicationPeriod: number;
  pricePerOne: number;
  pricePerPackage: number;
}

interface ResumesAccessOffer {
  id: number;
  areaId: number;
  professionId: number;
  accessDuration: number;
  numberOfContacts: number;
  price: number;
}

interface OfferDisplayProps {
  offer: {
    vacancyOffers: VacancyOffer[];
    resumesAccessOffers: ResumesAccessOffer[];
  };
}

export const OfferDisplay: React.FC<OfferDisplayProps> = ({ offer }) => {
  return (
    <div className={styles.offerContainer}>
      <h3>Предложенные услуги:</h3>
      {offer.vacancyOffers.length > 0 && (
        <div>
          <h4>Вакансии:</h4>
          {offer.vacancyOffers.map((offer) => (
            <div key={offer.id} className={styles.offerItem}>
              <p>Тип: {offer.vacancyType}</p>
              <p>Объем пакета: {offer.packageVolume}</p>
              <p>Период публикации: {offer.publicationPeriod}</p>
              <p>Цена за единицу: {offer.pricePerOne}</p>
              <p>Цена за пакет: {offer.pricePerPackage}</p>
            </div>
          ))}
        </div>
      )}
      {offer.resumesAccessOffers.length > 0 && (
        <div>
          <h4>Доступ к резюме:</h4>
          {offer.resumesAccessOffers.map((offer) => (
            <div key={offer.id} className={styles.offerItem}>
              <p>Длительность доступа: {offer.accessDuration}</p>
              <p>Количество контактов: {offer.numberOfContacts}</p>
              <p>Цена: {offer.price}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default OfferDisplay;
