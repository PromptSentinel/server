// index.js
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter, Routes, Route } from 'react-router-dom'; 

import Overview from './Overview';         
import ModelTests from './ModelTests';     
import Upload from './Upload';             

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Overview />} />           
        <Route path="/model-tests" element={<ModelTests />} /> 
        <Route path="/upload" element={<Upload />} />         
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

reportWebVitals();
