const axios = require('axios');

async function performLogin(userData) {
    try {

        // Отправляем запрос логина с полученным CSRF-токеном
        const loginResponse = await axios.post('http://localhost:8080/auth/loginup', userData, {
            withCredentials: true,
        });

        // Извлекаем и возвращаем JWT токен
        const jwtToken = loginResponse.data.accessToken;
        console.log('JWT token received:', jwtToken);
        return jwtToken;
    } catch (error) {
        console.error('Ошибка при выполнении логина:', error.response ? error.response.data : error.message);
        throw error;
    }
}

module.exports = { performLogin };