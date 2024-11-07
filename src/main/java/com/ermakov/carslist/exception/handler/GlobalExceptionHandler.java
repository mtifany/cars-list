package com.ermakov.carslist.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.ermakov.carslist.exception.BadRequestException;
import com.ermakov.carslist.exception.BrandNotFoundException;
import com.ermakov.carslist.exception.ModelNotFoundException;
import com.ermakov.carslist.exception.PhotoProcessingException;
import com.ermakov.carslist.exception.UserAlreadyExistsExceptiom;
import jakarta.validation.ValidationException;
import java.sql.SQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({BrandNotFoundException.class, ModelNotFoundException.class})
  public ResponseEntity<String> handleNotFoundException(RuntimeException exception) {
    return new ResponseEntity<>(exception.getMessage(), NOT_FOUND);
  }

  @ExceptionHandler({SQLException.class})
  public ResponseEntity<String> handleSQLException(SQLException exception) {
    return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<String> handleBindException(BindException exception) {
    return new ResponseEntity<>(exception.getBindingResult().toString(), BAD_REQUEST);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<String> handleValidationException(ValidationException exception) {
    return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
  }

  @ExceptionHandler({BadRequestException.class, PhotoProcessingException.class,
      UserAlreadyExistsExceptiom.class})
  public ResponseEntity<String> handleBadRequestException(Exception exception) {
    return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
  }
}
