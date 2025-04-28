import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export function useAuth() {
    const [token, setToken] = useState(() => {
        return localStorage.getItem('accessToken');
    });
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        if (token) {
            const tokenData = JSON.parse(atob(token.split('.')[1]));

            if (tokenData.exp * 1000 < Date.now()) {
                logout();
            }
        }
        setIsLoading(false);
    }, [token]);

    const fetchUserData = async () => {
        if (!token) return;

        try {
            const tokenData = JSON.parse(atob(token.split('.')[1]));
            const email = tokenData.email;
            console.log("Email from token:", email);
            
            const response = await fetch('http://localhost:8080/api/com.job.user.query.get_user_info', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email
                })
            });

            if (!response.ok) {
                throw new Error('Failed to fetch user data');
            }

            const userData = await response.json();
            setUser(userData);
        } catch (error) {
            console.error('Error fetching user data:', error);
            logout();
        }
    };

    const login = async (email, password) => {
        try {
            const response = await fetch('http://localhost:8080/api/com.job.user.login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (!response.ok) {
                throw new Error('Login failed');
            }

            const data = await response.json();
            const { accessToken } = data;

            localStorage.setItem('accessToken', accessToken);
            setToken(accessToken);
            
            await fetchUserData();

            navigate('/profile');

            return { success: true };
        } catch (error) {
            console.error('Login error:', error);
            return { success: false, error: error.message };
        }
    };

    const logout = () => {
        localStorage.removeItem('accessToken');
        setToken(null);
        setUser(null);
        navigate('/login');
    };

    const isAuthenticated = !!token;

    return {
        token,
        user,
        isLoading,
        isAuthenticated,
        login,
        logout,
        fetchUserData
    };
}
