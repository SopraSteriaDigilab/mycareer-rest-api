package application;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
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
@Validated
public class GlobalExceptionHandler
{

  /**
   * Method to catch and handle the MethodArgumentNotValidException exception.
   *
   * @param exception
   * @return A representation of the error(s) in a JSON like format (Map<String, Object>)
   */
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handle(MethodArgumentNotValidException exception)
  {
    return error(exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
        .collect(Collectors.toList()));
  }

  /**
   * Method to catch and handle the ConstraintViolationException exception.
   *
   * @param exception
   * @return A representation of the error(s) in a JSON like format (Map<String, Object>)
   */
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handle(ConstraintViolationException exception)
  {
    return error(
        exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
  }

  /**
   * Method to take an error object and map it to JSON like format.
   *
   * @param message
   * @return
   */
  private Map<String, Object> error(Object message)
  {
    return Collections.singletonMap("error", message);
  }
  
  /** Bean - Allows for parameter validation */
  @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
         return new MethodValidationPostProcessor();
    }
}