package ao.multaplus.exception.controller;

import ao.multaplus.exception.dto.ErrorResponseDTO;
import ao.multaplus.exception.model.NothingToUpdateException;
import ao.multaplus.exception.model.ResourceInConflictException;
import ao.multaplus.exception.model.ResourceNotFound;
import ao.multaplus.exception.model.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final HttpServletRequest request;

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> InvalidCredential(UnauthorizedException ex) {
        log.error(ex.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(),
                        "Internal Server Error", ex.getMessage(),
                        request.getRequestURI()));
    }
    @ExceptionHandler(ResourceInConflictException.class)
    public ResponseEntity<?> resourceInConflictExceptionHandler(ResourceInConflictException ex) {
        log.error(ex.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponseDTO(HttpStatus.CONFLICT.value(),
                        "Internal Server Error", ex.getMessage(),
                        request.getRequestURI()));
    }
    @ExceptionHandler(ResourceNotFound.class)
    ResponseEntity<ErrorResponseDTO> resourceNotFoundHandler(ResourceNotFound ex
    ) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Resource Not found",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(NothingToUpdateException.class)
    ResponseEntity<ErrorResponseDTO> NothingToUpdateExceptionHandler(NothingToUpdateException ex
    ) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                new ErrorResponseDTO(HttpStatus.NOT_MODIFIED.value(), "Resource Not Modifier",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponseDTO> internalErrorHandler(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(),
                        "Internal Server Error", ex.getMessage(),
                        request.getRequestURI()));
    }

}
