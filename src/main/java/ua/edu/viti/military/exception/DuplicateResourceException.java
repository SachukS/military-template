package ua.edu.viti.military.exception;

/**
 * Виключення при спробі створити дублікат ресурсу (409 Conflict)
 */
public class DuplicateResourceException extends BaseException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
}
