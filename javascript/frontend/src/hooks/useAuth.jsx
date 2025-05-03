import {useEffect, useState} from 'react';
import {apiCall} from '../utils/api';

export function useAuth() {
    const [token, setToken] = useState(() => {
        return localStorage.getItem('accessToken');
    });
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        setIsLoading(false);
    }, [token]);

    const getCurrentUser = () => {
        if (token) {
            const tokenData = JSON.parse(atob(token.split('.')[1]));

            if (tokenData.exp * 1000 < Date.now()) {
                logout();
            }

            const user = {
                userId: tokenData.userId,
                email: tokenData.email,
                role: tokenData.role,
            }

            console.log("User: " + JSON.stringify(user))

            return user
        }

        return null
    }

    const login = async (email, password) => {
        try {
            const {accessToken, accessTokenExpiresAt} = await apiCall('com.job.user.login',
                {
                    email: email,
                    password: password
                });

            localStorage.setItem('accessToken', accessToken);
            console.log("setToken: " + accessToken)
            setToken(accessToken);

            return {success: true};
        } catch (error) {
            console.error('Login error:', error);
            return {success: false, error: error.message};
        }
    };

    const logout = () => {
        localStorage.removeItem('accessToken');
        setToken(null);
        setUser(null);
    };

    return {
        token,
        user,
        isLoading,
        getCurrentUser,
        login,
        logout,
    };
}
