
const express = require('express');
const path = require('path');
const axios = require('axios');
const cookieParser = require('cookie-parser');
const authMiddleware = require('../middlewares/authMiddleware');
const { performLogin } = require('../utils/authUtils');

// Настройки для CSRF защиты с Axios
axios.defaults.xsrfCookieName = 'XSRF-TOKEN';
axios.defaults.xsrfHeaderName = 'X-XSRF-TOKEN';

const app = express();
const PORT = process.env.PORT || 3001;

// Middleware для обработки JSON и форм
app.use(express.json());
app.use(express.urlencoded({extended: true}));

// Middleware для статических файлов
app.use(express.static(path.join(__dirname, 'public')));
app.use(cookieParser());


// Настройки CORS
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', 'http://localhost:3000'); // Укажи реальный фронтенд-URL
  res.header('Access-Control-Allow-Credentials', 'true');
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
  res.header('Access-Control-Allow-Headers', 'Content-Type, X-XSRF-TOKEN');
  next();
});

// Главная страница
app.get('/', async (req, res) => {
  try {


    res.send(`
      <HTML>
        <head>
          <title>Home</title>
          <link rel="stylesheet" href="/stylesheets/style.css">
        </head> 
        <body>
          <div class="container">
            <h2>Добро пожаловать!</h2>
            <p>Нажмите на кнопку ниже, чтобы перейти к регистрации или авторизации</p>
            <button class="button button1" onclick="window.location.href='/auth/signup'">Перейти к регистрации</button>
             <button class="button button1" onclick="window.location.href='/auth/loginup'">Перейти к авторизации</button>
          </div>
        </body>
      </HTML>
    `);
  } catch (error) {
    console.error('Ошибка при загрузке товаров:', error);
    res.status(500).send('Неизвестная ошибка');
  }
});

app.get('/product',async (req, res) => {
    try{

        const response = await axios.get('http://localhost:8080/product', {
            headers: {
                Authorization: `Bearer ${req.cookies.accessToken}`
            },
            withCredentials: true });
        const products = response.data; // Ожидаем, что бэкенд вернет массив товаров

        const productListHTML = products.map(product => `
      <div class="product-container">
        <div class="product-info">
        <img src=${product.imageUrl} width="50" height="50">
        <h3>${product.name}</h3>
        </div>
      </div>
    `).join('');


    res.send(`
    <html">
    <head>
    <link rel="stylesheet" href="/stylesheets/style.css">
        <title>Products</title>
        </head>
        
        <body>
        <div class="text-container">
            Список товаров
        </div>
        <div class="products">
            ${productListHTML}
        </div>
        </body>
    </html>
    `);
    }catch(error){
        console.error('Ошибка при загрузке товаров:', error);
        res.status(500).send('Ошибка загрузки товаров');
    }
});

// Страница регистрации
app.get('/auth/signup', (req, res) => {
    res.send(`
    <html>
      <head>
        <title>Регистрация</title>
      </head>
      <body>
        <h2>Регистрация</h2>
        <form action="/auth/signup" method="POST">
          <label for="username">Имя пользователя:</label>
          <input type="text" id="username" name="username" required>
          <br>
          <label for="password">Пароль:</label>
          <input type="password" id="password" name="password" required>
          <br>
          <label for="email">Почта:</label>
          <input type="email" id="email" name="email" required>
          <br>
          <button type="submit">Зарегистрироваться</button>
        </form>
      </body>
    </html>
  `);
});

// Обработка отправки данных на сервер Spring
function getCSRFTokenFromCookies(req) {
  const cookies = req.cookies || {};
  return cookies['XSRF-TOKEN'];  // Возвращаем значение токена
}

// POST endpoint для регистрации
app.post('/auth/signup', async (req, res) => {
    try {
        console.log('Request body:', req.body);
        const userData = req.body;

        const signupResponse = await axios.post('http://localhost:8080/auth/signup', userData, {
            withCredentials: true
        });

        console.log('Success:', signupResponse.data);
        res.status(200).send(`
      <html>
        <head><title>Successfully!</title></head>
        <body>
          <h2>Вы успешно зарегистрировались</h2>
          <form action="/" method="GET">
            <br>
            <button type="submit">Зарегистрироваться</button>
          </form>
        </body>
      </html>
    `);
    } catch (error) {
        console.error('Ошибка при регистрации:', error);
        res.status(500).send('Ошибка при регистрации');
    }
});


app.get('/auth/loginup', async (req, res) => {
  res.send(`
    <html>
      <head>
        <title>Авторизация</title>
      </head>
      <body>
        <h2>Авторизоваться</h2>
        <form action="/auth/loginup" method="POST">
          <label for="username">Имя пользователя или почта:</label>
          <input type="text" id="username" name="username" required>
          <br>
          <label for="password">Пароль:</label>
          <input type="password" id="password" name="password" required>
          <button type="submit">Авторизоваться</button>
        </form>
      </body>
    </html>
  `);
})


app.post('/auth/loginup', async (req, res) => {
  try {
      console.log('Request body:', req.body);
      const userData = req.body;
      const token = performLogin(userData);

      res.cookie('accessToken', await token, {
          httpOnly: true,
          secure: true, // true для HTTPS, false для разработки
          sameSite: 'Strict'
      });

      res.status(200).send(`
          <html>
            <head>
              <title>Successfully!</title>
            </head>
            <body>
              <h2>Вы успешно авторизировались</h2>
              <form action="/product" method="GET">
                <br>
                <button type="submit">Главное меню</button>
              </form>
            </body>
          </html>
        `);
  } catch (error) {
          console.error('Ошибка авторизации:', error);
          res.status(401).send('Ошибка авторизации');
        }
});


app.get('/profile', async (req, res) => {
    try {

        // Получаем данные пользователя
        const userResponse = await axios.get('http://localhost:8080/user/me', {
            headers: {
                Authorization: `Bearer ${req.cookies.accessToken}` },
            withCredentials: true, // Включаем отправку cookies
             });

        const user = userResponse.data;

        // Отправляем HTML с данными пользователя
        res.send(`
      <html>
        <head>
          <title>Личный кабинет</title>
          <link rel="stylesheet" href="/stylesheets/style.css">
        </head>
        <body>
          <div class="container">
            <h2>Личный кабинет</h2>
            <p><strong>Имя:</strong> ${user.name}</p>
            <p><strong>Email:</strong> ${user.email}</p>
            <p><strong>Баланс:</strong> ${user.balance} ₽</p>
            
            <h3>Купленные товары:</h3>
            <ul>
              ${user.purchases.map(p => `<li>${p.name} - ${p.price} ₽</li>`).join('')}
            </ul>

            <button onclick="window.location.href='/auth/logout'">Выйти</button>
          </div>
        </body>
      </html>
    `);
    } catch (error) {
        res.redirect('/auth/loginup');
        console.error('Ошибка загрузки профиля:', error);
    }
});

app.get('/auth/logout', async (req, res) => {
    res.clearCookie('accessToken', {
        httpOnly: true,
        secure: true,
        sameSite: 'Strict',
    });
    res.redirect('/auth/loginup'); // Перенаправляем на страницу входа
});

// Запуск сервера
app.listen(PORT, () => {
  console.log(`API is listening on port ${PORT}`);
});

module.exports = app;
