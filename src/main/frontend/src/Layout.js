// Layout.js
import React from 'react';
import { NavLink } from 'react-router-dom'; // NavLink를 사용해 active 상태 자동 적용

function Layout({ children }) {
  return (
    <div>
      <header className="navbar">
        <div className="navbar-brand">
          <img 
            src="https://img.icons8.com/?size=100&id=42849&format=png&color=000000" 
            alt="Logo" 
            className="logo" 
          />
          Prompt Sentinel
        </div>
        <nav className="nav-menu">
          <NavLink 
            to="/" 
            className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
          >
            Overview
          </NavLink>
          <NavLink 
            to="/model-tests" 
            className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
          >
            Model Tests
          </NavLink>
          <NavLink 
            to="/upload"  
            className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
          >
            Upload
          </NavLink>
        </nav>
      </header>

      <main className="container">
        {children} 
      </main>
    </div>
  );
}

export default Layout;
