package org.example.mytomcat.exception;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class NotFoundContextPathException extends RuntimeException{
    public NotFoundContextPathException() {
    }

    public NotFoundContextPathException(String message) {
        super(message);
    }

    public NotFoundContextPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundContextPathException(Throwable cause) {
        super(cause);
    }

    public NotFoundContextPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
