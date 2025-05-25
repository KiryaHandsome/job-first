import React, {useEffect, useState} from 'react';
import {Link, useNavigate, useLocation} from 'react-router-dom';
import {useAuth} from '../hooks/useAuth';
import './Navigation.css';
import {EMPLOYER_ROLE} from "../constants/Common.jsx";

const Navigation = ({isAuthenticated}) => {
    const navigate = useNavigate();
    const location = useLocation();
    const {getCurrentUser, logout} = useAuth();
    const [isEmployer, setIsEmployer] = useState(false);

    useEffect(() => {
        const user = getCurrentUser()

        if (user) {
            setIsEmployer(user.role === EMPLOYER_ROLE);
        } else {
            setIsEmployer(false);
        }
    }, [getCurrentUser]);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    // Функция для проверки активного пути
    const isActive = (path) => {
        return location.pathname === path ||
            (path !== '/' && location.pathname.startsWith(path));
    };

    return (
        <nav className="navigation">
            <div className="nav-container">
                <div className="nav-logo">
                    <Link to="/">Job First</Link>
                </div>
                <div className="nav-links">
                    {isEmployer ? (
                        <></>
                    ) : <Link
                        to="/vacancies"
                        className={`nav-link ${isActive('/vacancies') ? 'active' : ''}`}
                    >
                        Вакансии
                    </Link>}


                    {isAuthenticated ? (
                        <>
                            {isEmployer ? (
                                <>
                                    <Link
                                        to="/employer-vacancies"
                                        className={`nav-link ${isActive('/employer-vacancies') ? 'active' : ''}`}
                                    >
                                        Вакансии
                                    </Link>
                                    <Link
                                        to="/create-vacancy"
                                        className={`nav-link ${isActive('/create-vacancy') ? 'active' : ''}`}
                                    >
                                        Создать Вакансию
                                    </Link>
                                    <Link
                                        to="/company"
                                        className={`nav-link ${isActive('/company') ? 'active' : ''}`}
                                    >
                                        Компания
                                    </Link>
                                </>
                            ) : (
                                <Link
                                    to="/resumes"
                                    className={`nav-link ${isActive('/resumes') ? 'active' : ''}`}
                                >
                                    Мои резюме
                                </Link>
                            )}
                            <Link
                                to="/profile"
                                className={`nav-link ${isActive('/profile') ? 'active' : ''}`}
                            >
                                Профиль
                            </Link>
                            <button
                                onClick={handleLogout}
                                className="logout-button"
                            >
                                Выйти
                            </button>
                        </>
                    ) : (
                        <>
                            <Link
                                to="/login"
                                className={`nav-link ${isActive('/login') ? 'active' : ''}`}
                            >
                                Войти
                            </Link>
                            <Link
                                to="/register"
                                className={`nav-link ${isActive('/register') ? 'active' : ''}`}
                            >
                                Зарегистрироваться
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navigation;