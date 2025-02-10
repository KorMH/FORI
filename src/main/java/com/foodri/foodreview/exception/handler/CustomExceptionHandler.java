package com.foodri.foodreview.exception.handler;

import com.foodri.foodreview.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<ErrorDto> handleCustom400Exception(CustomException ex){
    return ErrorDto.toResponseEntity(ex);
  }
}
