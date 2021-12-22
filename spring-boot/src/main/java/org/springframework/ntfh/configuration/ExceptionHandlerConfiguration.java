package org.springframework.ntfh.configuration;

import java.util.Map;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.exceptions.BannedUserException;
import org.springframework.ntfh.exceptions.MaximumLobbyCapacityException;
import org.springframework.ntfh.exceptions.MissingAttributeException;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.exceptions.UserAlreadyInLobbyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
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

    private static ResponseEntity<Object> buildResponseEntity(String message, HttpStatus status) {
        Map<String, Object> body = Map.of("timestamp", new java.util.Date(), "status", status.value(), "error",
                status.getReasonPhrase(), "message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> dataAccessExceptionHandler(HttpServletRequest request, DataAccessException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> dataIntegrityViolationExceptionHandler(HttpServletRequest request,
            DataIntegrityViolationException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NonMatchingTokenException.class)
    public ResponseEntity<Object> nonMatchingTokenExceptionHandler(HttpServletRequest request,
            NonMatchingTokenException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentExceptionHandler(HttpServletRequest request,
            IllegalArgumentException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaximumLobbyCapacityException.class)
    public ResponseEntity<Object> maximumLobbyCapacityExceptionHandler(HttpServletRequest request,
            MaximumLobbyCapacityException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BannedUserException.class)
    public ResponseEntity<Object> bannedUserExceptionHandler(HttpServletRequest request,
            BannedUserException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserAlreadyInLobbyException.class)
    public ResponseEntity<Object> userAlreadyInLobbyExceptionHandler(HttpServletRequest request,
            UserAlreadyInLobbyException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingAttributeException.class)
    public ResponseEntity<Object> missingAttributeExceptionHandler(HttpServletRequest request,
            MaximumLobbyCapacityException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Validator for annotation restrictions in entities
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationExceptionHandler(HttpServletRequest request,
            ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .reduce((s1, s2) -> s1 + ". " + s2).orElse("");
        return buildResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<Object> rollbackExceptionHandler(HttpServletRequest request,
            RollbackException ex) {
        // TODO redirect this exception to its appropiate handler so the return message
        // is clearer
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> numberFormatExceptionHandler(HttpServletRequest request,
            NumberFormatException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String message = ex.getBindingResult().getAllErrors().stream().map(e -> e.getDefaultMessage())
                .reduce((s1, s2) -> s1 + ". " + s2).orElse("");
        return buildResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

    // TODO add more custom exceptions here. The structure is the same, the only
    // thing changing should be the HttpStatus
}