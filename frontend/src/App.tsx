import React from 'react';
import Header from './components/Header/Header';
import RegionSelector from './components/RegionSelector/RegionSelector';

function App() {
  return (
    <div className="App">
      <Header />
      <div style={{
        width: '1000px',
        margin: '100 auto',
        padding: '200px 0' // Добавляем отступ сверху и снизу при необходимости
      }}>
        <RegionSelector />
      </div>
      <p>HELLO</p>
    </div>
  );
}

export default App;