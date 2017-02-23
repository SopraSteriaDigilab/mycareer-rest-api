package application;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception Handler for the rest controller to handle validated objects and validated params
 *
 */
@ControllerAdvice
@Component
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler
{
  /** List of constants to represent error messages */
  public static final String ERROR_EMPTY = "Please fill in all the required feilds.";
  public static final String ERROR_DESCRIPTION_LIMIT_1000 = "Max Description length is 1000 characters.";
  public static final String ERROR_DESCRIPTION_LIMIT_2000 = "Max Description length is 1000 characters.";
  public static final String ERROR_NAME_LIMIT = "Max Name length is 150 characters.";
  public static final String ERROR_TITLE_LIMIT = "Max Title is 150 characters.";
  public static final String ERROR_PROGRESS_LIMIT = "Please enter a valid progress (Proposed, In-Progress, Complete).";
  
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map handle(MethodArgumentNotValidException exception)
  {
    return error(exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
        .collect(Collectors.toList()));
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map handle(ConstraintViolationException exception)
  {
    return error(
        exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
  }

  private Map error(Object message)
  {
    return Collections.singletonMap("error", message);
  }
  
}