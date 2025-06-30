import React from "react";
import { Routes, Route, useLocation } from "react-router-dom";
import styles from "./App.module.css";
import Header from "./pages/ServicePickerPage/components/Header/Header";
import AdminPage from "./pages/ServicePickerPage/AdminPage";
import ClientPage from "./pages/ServicePickerPage/ClientPage";

const App: React.FC = () => {
  const location = useLocation();
  const headerTitle =
    location.pathname === "/admin" ? "Админ-панель" : "Подборщик услуг";

  return (
    <div className={styles.app}>
      <Header title={headerTitle} />
      <Routes>
        <Route path="/" element={<ClientPage />} />
        <Route path="/admin" element={<AdminPage />} />
      </Routes>
    </div>
  );
};

export default App;
