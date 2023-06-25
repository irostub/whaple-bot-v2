package com.irostub.market.interceptor;

public class TelegramAuthException extends RuntimeException {
    public TelegramAuthException() {
        super();
    }

    public TelegramAuthException(String message) {
        super(message);
    }

    public TelegramAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelegramAuthException(Throwable cause) {
        super(cause);
    }
}
