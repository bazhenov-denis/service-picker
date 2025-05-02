// src/components/Header/index.tsx
import { FC } from "react";
import styles from "./Header.module.css"; // Импорт CSS-модуля

const Header: FC = () => {
  return (
    <div className={styles.headerContainer}>
      <img className={styles.logo} src="/hh-logo.svg" alt="Логотип" />
      <h1 className={styles.title}>Подборщик услуг</h1>
    </div>
  );
};

export default Header;
