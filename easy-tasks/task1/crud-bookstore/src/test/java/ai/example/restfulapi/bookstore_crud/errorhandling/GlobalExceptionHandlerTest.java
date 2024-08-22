package ai.example.restfulapi.bookstore_crud.errorhandling;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void init() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void handleEntityNotFoundReturnsNotFoundResponse() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEntityNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody().getMessage());
    }


    @Test
    public void handleValidationExceptionsReturnsBadRequestResponse() {

        MethodArgumentNotValidException ex = getValidationException();
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        var result = handler.handleValidationExceptions(ex);
        var body = (Map<String, String>) result.getBody();

        assert result.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert body.containsKey("field1");
        assert body.get("field1").equals("Field cannot be empty");

    }

    private MethodArgumentNotValidException getValidationException() {

        HashMap<String, Object> targetMap = new HashMap<>();
        BindingResult bindingResult = new MapBindingResult(targetMap, "target");
        bindingResult.addError(new FieldError("objectName", "field1", "Field cannot be empty"));
        return new MethodArgumentNotValidException(null, bindingResult);
    }


    @Test
    public void handleGeneralExceptionReturnsInternalServerErrorResponse() {
        Exception ex = new Exception("An unexpected error occurred. Please try again.");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred. Please try again.", response.getBody().getMessage());
    }
}