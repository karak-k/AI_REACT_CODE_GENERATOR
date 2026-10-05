package com.codingshuttle.projects.lovable_clone.error;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIError> handleBadRequest(BadRequestException ex)
    {
        APIError apiError= new APIError(HttpStatus.BAD_REQUEST,ex.getMessage());
        log.error(apiError.toString(),ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleResourceNotFound(ResourceNotFoundException ex)
    {
        APIError apiError= new APIError(HttpStatus.NOT_FOUND,ex.getResourceName() + " With Id " + ex.getResourceId()+" Not Found ");
        log.error(apiError.toString(),ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> handleInputValidtionError(MethodArgumentNotValidException ex)
    {
        List<APIFieldError> errors= ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new APIFieldError(error.getField(),error.getDefaultMessage()))
                .toList();
        APIError apiError= new APIError(HttpStatus.BAD_REQUEST,"Input Validation Failed.",errors);
        log.error(apiError.toString(),ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<APIError> handleUsernameNotFound(UsernameNotFoundException ex) {

        APIError apiError = new APIError(
                HttpStatus.NOT_FOUND,
                "Username Not Found with Username:" +ex.getMessage()
        );

        log.error(apiError.toString(), ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<APIError> handleAuthenticationException(AuthenticationException ex) {

        APIError apiError = new APIError(
                HttpStatus.UNAUTHORIZED,
                "Authentication Failed: " +ex.getMessage()
        );

        log.error(apiError.toString(), ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<APIError> handleJwtException(JwtException ex) {

        APIError apiError = new APIError(
                HttpStatus.UNAUTHORIZED,
                "Invalid JWT Token: " + ex.getMessage()
        );

        log.error(apiError.toString(), ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIError> handleAccessDeniedException(AccessDeniedException ex) {

        APIError apiError = new APIError(
                HttpStatus.FORBIDDEN,
                "Access denied: Insufficient permissions." +ex.getMessage()
        );

        log.error(apiError.toString(), ex);

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

}
