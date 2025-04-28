import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';
import { routes } from './routes/routes';
import { PrivateRoute } from './routes/PrivateRoutes';
import { Layout } from './components/layout/Layout';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                {routes.map((route) => (
                    <Route
                        key={route.path}
                        path={route.path}
                        element={
                            route.public ? (
                                route.element
                            ) : (
                                <PrivateRoute>
                                    {route.layout ? (
                                        <Layout>
                                            {route.element}
                                        </Layout>
                                    ) : (
                                        route.element
                                    )}
                                </PrivateRoute>
                            )
                        }
                    />
                ))}
                <Route path="/" element={<Navigate to="/login" replace />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
