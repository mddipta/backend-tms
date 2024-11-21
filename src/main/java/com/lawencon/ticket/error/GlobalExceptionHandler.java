package com.lawencon.ticket.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.lawencon.ticket.model.response.WebResponse;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(fieldError -> fieldError.getField(),
                        fieldError -> List.of(fieldError.getDefaultMessage()),
                        (messages1, messages2) -> {
                            List<String> combinedMessages = new ArrayList<>(messages1);
                            combinedMessages.addAll(messages2);
                            return combinedMessages;
                        }));

        WebResponse<Object> errorResponse =
                WebResponse.builder().code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase()).errors(errors)
                        .message("Validation failed").build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<WebResponse<Object>> handleAccessDenied(HttpServletRequest request,
            AccessDeniedException ex) {
        String message;
        if (request.getUserPrincipal() == null) {
            message = "You must be logged in to access this resource.";
        } else {
            message = "User does not have permission to access this resource.";
        }
        WebResponse<Object> errorResponse =
                WebResponse.builder().code(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED.getReasonPhrase()).message(message).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
