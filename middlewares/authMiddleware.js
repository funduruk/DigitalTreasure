// middlewares/authMiddleware.js
const jwt = require('jsonwebtoken');
require('dotenv').config();

function authMiddleware(req, res, next) {
    const token = req.cookies.accessToken;
    if (!token) {
        return res.status(401).json({ message: 'Необходима авторизация' });
    }
    try {
        const secret = process.env.JWT_SECRET;
        if (!secret) {
            throw new Error('Секретный ключ JWT не задан');
        }
        req.user = jwt.verify(token);
        next();
    } catch (err) {
        return res.status(403).json({ message: 'Недействительный токен \n  ' + err });
    }
}

module.exports = authMiddleware;
