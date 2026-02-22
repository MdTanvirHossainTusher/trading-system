package com.doin.signal.payload.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public final class ResponseBuilder {

    private ResponseBuilder() {}
    
    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        return success(HttpStatus.OK, "Operation successful", null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return success(HttpStatus.OK, "Operation successful", data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return success(HttpStatus.OK, message, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return success(HttpStatus.CREATED, "Resource created successfully", data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return success(HttpStatus.CREATED, message, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .data(data)
                .errors(null)
                .build();
        
        return ResponseEntity.status(status).body(response);
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return error(status, message, "ERROR");
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message, String errorType) {
        ErrorDetail error = ErrorDetail.builder()
                .message(message)
                .type(errorType)
                .build();
        
        return error(status, Collections.singletonList(error));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, List<ErrorDetail> errors) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status("error")
                .message(extractPrimaryMessage(errors))
                .statusCode(String.valueOf(status.value()))
                .data(null)
                .errors(errors)
                .build();
        
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> validationError(List<ErrorDetail> errors) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, errors);
    }
    
    private static String extractPrimaryMessage(List<ErrorDetail> errors) {
        if (errors == null || errors.isEmpty()) {
            return "An error occurred";
        }
        return errors.size() == 1 ?
                errors.get(0).getMessage() : String.format("%d validation errors occurred", errors.size());
    }
}