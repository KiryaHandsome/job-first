import {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import './LoginPage.css';
import {Link} from 'react-router-dom';
import {FaUserPlus} from 'react-icons/fa';
import {useAuth} from '../../hooks/useAuth';

export function LoginPage({setIsAuthenticated}) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const {login, isAuthenticated} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (isAuthenticated) {
            setIsAuthenticated(true);
            navigate('/profile');
        }
    }, [isAuthenticated, navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const result = await login(email, password);

            if (!result.success) {
                setError('Неверный email или пароль');
            } else {
                setIsAuthenticated(true);
                navigate('/profile');
            }
        } catch (err) {
            setError('Произошла ошибка при входе');
            console.error('Login error:', err);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="login-container">
            <form onSubmit={handleSubmit} className="login-form">
                <h2>Вход в систему</h2>

                {error && <div className="error-message">{error}</div>}

                <div className="form-group">
                    <label htmlFor="email">Почта</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Введите ваш email"
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="password">Пароль</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Введите ваш пароль"
                        required
                    />
                </div>

                <button type="submit" className="login-button" disabled={isLoading}>
                    {isLoading ? 'Вход...' : 'Войти'}
                </button>

                <div className="register-link-container">
                    <span className="register-text">Еще не зарегистрированы?</span>
                    <Link to="/register" className="register-link">
                        <FaUserPlus className="register-icon"/>
                        Зарегистрироваться
                    </Link>
                </div>
            </form>
        </div>
    );
}