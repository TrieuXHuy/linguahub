package com.huy.linguahub.controller.error;

import com.huy.linguahub.service.dto.response.error.ResErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler({ResourceAlreadyExistsException.class,
            ResourceNotFoundException.class,
            UsernameNotFoundException.class})
    public ResponseEntity<ResErrorDTO> handleAllExceptions(Exception ex, HttpServletRequest request) {
        ResErrorDTO resErrorDTO = mapErrorDTO(ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resErrorDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResErrorDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        Object message = errors.size() == 1 ? errors.getFirst() : errors;

        ResErrorDTO resErrorDTO = ResErrorDTO.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)  // Lưu lỗi ở đây
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resErrorDTO);
    }

    public ResErrorDTO mapErrorDTO(Exception ex, HttpServletRequest request) {
        return ResErrorDTO.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }
}

