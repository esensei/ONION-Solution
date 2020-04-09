package ru.myword.service.exception;

/**
 *
 * @author Губа Георгий
 */
public class BaseException extends RuntimeException
{
    public BaseException(String message)
    {
        super(message);
    }

    public BaseException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
