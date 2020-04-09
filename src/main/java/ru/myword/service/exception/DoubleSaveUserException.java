package ru.myword.service.exception;

/**
 * Ошибка повтороного сохранения пользователя
 *
 * @author Губа Георгий
 */
public class DoubleSaveUserException extends BaseException
{
    public DoubleSaveUserException(String message)
    {
        super(message);
    }

    public DoubleSaveUserException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
