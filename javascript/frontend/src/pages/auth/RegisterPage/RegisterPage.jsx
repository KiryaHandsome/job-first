import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import './RegisterPage.css';
import {useAuth} from "../../../hooks/useAuth.jsx";
import {EMPLOYER_ROLE, USER_ROLE} from "../../../constants/Common.jsx";

const RegisterPage = () => {
    const navigate = useNavigate();
    const {register} = useAuth();
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: '',
        role: USER_ROLE // Default role
    });
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const toggleRole = () => {
        setFormData(prevState => ({
            ...prevState,
            role: prevState.role === USER_ROLE ? EMPLOYER_ROLE : USER_ROLE
        }));
    };

    const validateForm = () => {
        if (!formData.email || !formData.password || !formData.confirmPassword || !formData.firstName || !formData.lastName) {
            setError('Все поля обязательны для заполнения');
            return false;
        }

        if (formData.password !== formData.confirmPassword) {
            setError('Пароли не совпадают');
            return false;
        }

        if (formData.password.length < 6) {
            setError('Пароль должен содержать минимум 6 символов');
            return false;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(formData.email)) {
            setError('Введите корректный email адрес');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        if (!validateForm()) {
            return;
        }

        try {
            setLoading(true);
            const result = await register({
                email: formData.email,
                password: formData.password,
                firstName: formData.firstName,
                lastName: formData.lastName,
                role: formData.role
            });

            if (!result.success) {
                setError('Неверный email или пароль');
            } else {
                navigate('/profile');
            }
        } catch (err) {
            setError(err.message || 'Произошла ошибка при регистрации');
        } finally {
            setLoading(false);
        }
    };

    const handleLoginClick = () => {
        navigate('/login');
    };

    return (
        <div className="register-container">
            <div className="register-form-container">
                <h1>Регистрация</h1>
                {error && <div className="error-message">{error}</div>}

                <div className="role-toggle">
                    <span className={formData.role === USER_ROLE ? 'active' : ''}>Соискатель</span>
                    <label className="switch">
                        <input
                            type="checkbox"
                            checked={formData.role === EMPLOYER_ROLE}
                            onChange={toggleRole}
                        />
                        <span className="slider round"></span>
                    </label>
                    <span className={formData.role === EMPLOYER_ROLE ? 'active' : ''}>Работодатель</span>
                </div>

                <form onSubmit={handleSubmit} className="register-form">
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            placeholder="Введите email"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="firstName">Имя</label>
                        <input
                            type="text"
                            id="firstName"
                            name="firstName"
                            value={formData.firstName}
                            onChange={handleChange}
                            placeholder="Введите имя"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="lastName">Фамилия</label>
                        <input
                            type="text"
                            id="lastName"
                            name="lastName"
                            value={formData.lastName}
                            onChange={handleChange}
                            placeholder="Введите фамилию"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Пароль</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            placeholder="Введите пароль"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirmPassword">Подтвердите пароль</label>
                        <input
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            placeholder="Подтвердите пароль"
                            required
                        />
                    </div>

                    <button type="submit" className="register-button" disabled={loading}>
                        {loading ? 'Регистрация...' : 'Зарегистрироваться'}
                    </button>
                </form>

                <div className="login-link">
                    Уже есть аккаунт?{' '}
                    <button type="button" className="link-button" onClick={handleLoginClick}>
                        Войти
                    </button>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage; 