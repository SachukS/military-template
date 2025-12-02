package ua.edu.viti.military.exception;

/**
 * Виключення коли ресурс не знайдено (404)
 */
public class ResourceNotFoundException extends BaseException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
