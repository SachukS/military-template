package ua.edu.viti.military.exception;

/**
 * Базовий клас для всіх власних виключень
 */
public abstract class BaseException extends RuntimeException {
    
    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
