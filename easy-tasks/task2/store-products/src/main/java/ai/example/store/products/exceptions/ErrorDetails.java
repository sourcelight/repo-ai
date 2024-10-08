/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.store.products.exceptions;


import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    // Getters and Setters
}