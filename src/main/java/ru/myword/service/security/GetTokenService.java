package ru.myword.service.security;

import ru.myword.service.exception.BaseException;

/**
 * Сервис по получению jwt-токена
 *
 * @author Maria Amend
 * @since 28.10.2019
 */
public interface GetTokenService
{
    /**
     * Получение jwt токена
     *
     * @param username логин пользователя
     * @param password пароль пользователя
     * @return зашифрованный jwt токен
     */
    String getToken(String username, String password) throws BaseException;
}
