package org.springframework.samples.ntfh.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.ntfh.exceptions.InvalidValueException;
import org.springframework.samples.ntfh.exceptions.MaximumLobbyCapacityException;
import org.springframework.samples.ntfh.exceptions.MissingAttributeException;
import org.springframework.samples.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This advice is necessary because MockMvc is not a real servlet environment,
 * therefore it does not redirect error responses to [ErrorController], which
 * produces validation response. So we need to fake it in tests. It's not ideal,
 * but at least we can use classic MockMvc tests for testing error response +
 * document it.
 */
@ControllerAdvice
public class ExceptionHandlerConfiguration extends ResponseEntityExceptionHandler {
    @Autowired
    private BasicErrorController errorController;

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> dataAccessExceptionHandler(HttpServletRequest request, DataAccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NonMatchingTokenException.class)
    public ResponseEntity<String> nonMatchingTokenExceptionHandler(HttpServletRequest request,
            NonMatchingTokenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MaximumLobbyCapacityException.class)
    public ResponseEntity<String> maximunLobbyCapacityExceptionHandeler(HttpServletRequest request, MaximumLobbyCapacityException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingAttributeException.class)
    public ResponseEntity<String> missingAttributeExceptionHandeler(HttpServletRequest request, MaximumLobbyCapacityException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<String> InvalidValueException(HttpServletRequest request, MaximumLobbyCapacityException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    // TODO add more custom exceptions here. The structure is the same, the only
    // thing changing should be the HttpStatus
}