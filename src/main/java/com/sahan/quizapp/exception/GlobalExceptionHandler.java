package com.sahan.quizapp.exception;

import com.sahan.quizapp.response.ApiResponse;
import com.sahan.quizapp.response.ErrorDto;
import com.sahan.quizapp.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorDto> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(new ErrorDto(error.getField(), error.getDefaultMessage())));
        ApiResponse<Object> api = new ApiResponse<>(false, null, "Validation failed", errors);
        return new ResponseEntity<>(api, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(v -> {
            String path = v.getPropertyPath().toString();
            errors.add(new ErrorDto(path, v.getMessage()));
        });
        ApiResponse<Object> api = new ApiResponse<>(false, null, "Validation failed", errors);
        return new ResponseEntity<>(api, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Parameter '%s' has invalid value '%s'", ex.getName(), ex.getValue());
        List<ErrorDto> errors = new ArrayList<>();
        errors.add(new ErrorDto(ex.getName(), message));
        ApiResponse<Object> api = new ApiResponse<>(false, null, "Type mismatch", errors);
        return new ResponseEntity<>(api, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Object>> handleAll(Exception ex) {
        List<ErrorDto> errors = new ArrayList<>();
        errors.add(new ErrorDto(null, ex.getMessage() != null ? ex.getMessage() : "unexpected error"));
        ApiResponse<Object> api = new ApiResponse<>(false, null, "Server error", errors);
        return new ResponseEntity<>(api, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        errors.add(new ErrorDto(null, ex.getMessage()));
        ApiResponse<Object> api = new ApiResponse<>(false, null, "Not found", errors);
        return new ResponseEntity<>(api, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
