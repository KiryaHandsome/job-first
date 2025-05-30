const BACKEND_BASE_URL = 'http://localhost:8080';
const API_BASE_URL = BACKEND_BASE_URL + "/api";

// Получаем токен из localStorage
const getAuthToken = () => {
    return localStorage.getItem('accessToken');
};

const getHeaders = () => {
    const token = getAuthToken();

    return {
        'Content-Type': 'application/json',
        ...(token ? {'Authorization': `Bearer ${token}`} : {})
    };
};

// Общая функция для выполнения запросов
export const apiRequest = async (endpoint, options = {}) => {
    const url = `${API_BASE_URL}/${endpoint}`;
    const headers = getHeaders();

    try {
        const response = await fetch(url, {
            ...options,
            headers: {
                ...headers,
                ...options.headers
            }
        });

        if (!response.ok || response.code != null) {

            // Если получили 401, возможно токен истек
            if (response.status === 401) {
                localStorage.removeItem('accessToken');
                window.location.href = '/login';
                throw new Error('Unauthorized');
            }
            throw new Error(`API request failed: ${response.statusText}`);
        }

        const result = await response.json()

        console.log("Response: " + JSON.stringify(result))

        return result
    } catch (error) {
        console.error('API request error:', error);
        throw error;
    }
};

export const apiCall = (endpoint, data = {}) => {
    const body = JSON.stringify(data)
    console.log('Body:', body);
    return apiRequest(endpoint, {
        method: 'POST',
        body: body
    });
};