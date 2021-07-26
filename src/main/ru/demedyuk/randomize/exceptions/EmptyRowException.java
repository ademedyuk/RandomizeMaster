package ru.demedyuk.randomize.exceptions;

public class EmptyRowException extends Exception {

    public EmptyRowException() {
        super();
    }

    public EmptyRowException(String message) {
        super(message);
    }

    public EmptyRowException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyRowException(Throwable cause) {
        super(cause);
    }

    protected EmptyRowException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
