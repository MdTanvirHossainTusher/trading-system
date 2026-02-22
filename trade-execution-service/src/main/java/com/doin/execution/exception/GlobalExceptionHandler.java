package com.doin.execution.exception;

import com.doin.execution.payload.response.ApiResponse;
import com.doin.execution.payload.response.ErrorDetail;
import com.doin.execution.payload.response.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static com.doin.execution.constant.ErrorTypes.*;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());

        List<ErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorDetail.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .type(VALIDATION_ERROR)
                        .build())
                .collect(Collectors.toList());

        return ResponseBuilder.validationError(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, ex.getMessage(), NOT_FOUND_ERROR);
    }

    @ExceptionHandler(SignalParseException.class)
    public ResponseEntity<ApiResponse<Object>> handleParseException(SignalParseException ex) {
        log.error("Signal parse issue: {}", ex.getMessage());
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, ex.getMessage(), BAD_REQUEST_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), INTERNAL_ERROR);
    }
}
