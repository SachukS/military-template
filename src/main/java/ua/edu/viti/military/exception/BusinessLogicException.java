package ua.edu.viti.military.exception;

/**
 * Виключення при порушенні бізнес-логіки (400 Bad Request)
 */
public class BusinessLogicException extends BaseException {
    
    public BusinessLogicException(String message) {
        super(message);
    }
}
