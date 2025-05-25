import React, {useState} from 'react';
import {BrowserRouter} from 'react-router-dom';
import './App.css';
import AppRoutes from "./routes/AppRoutes.jsx";
import Navigation from "./components/Navigation.jsx";

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(
        localStorage.getItem("accessToken") != null
    );

    return (
        <BrowserRouter>
            <div className="app">
                <Navigation isAuthenticated={isAuthenticated}/>
                <main className="main-content">
                    <AppRoutes setIsAuthenticated={setIsAuthenticated}/>
                </main>
            </div>
        </BrowserRouter>
    );
}

export default App;
