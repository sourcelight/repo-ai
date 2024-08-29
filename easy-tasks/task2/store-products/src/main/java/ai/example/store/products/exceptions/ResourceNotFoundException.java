/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.store.products.exceptions;


public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}