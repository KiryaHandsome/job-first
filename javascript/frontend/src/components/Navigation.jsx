import { Link, useLocation } from 'react-router-dom';
import './Navigation.css';

export function Navigation() {
    const location = useLocation();

    return (
        <nav className="navigation">
            <div className="nav-container">
                <Link 
                    to="/vacancies" 
                    className={`nav-link ${location.pathname === '/vacancies' ? 'active' : ''}`}
                >
                    Вакансии
                </Link>
                <Link 
                    to="/resumes" 
                    className={`nav-link ${location.pathname === '/resumes' ? 'active' : ''}`}
                >
                    Мои резюме
                </Link>
                <Link 
                    to="/profile" 
                    className={`nav-link ${location.pathname === '/profile' ? 'active' : ''}`}
                >
                    Профиль
                </Link>
            </div>
        </nav>
    );
} 