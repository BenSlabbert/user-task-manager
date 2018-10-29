package com.user.manager.api.advice;

import com.user.manager.api.exception.APIException;
import com.user.manager.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler({APIException.class})
  public ResponseEntity<APIResponse> handleAPIException(Exception ex) {
    return ResponseEntity.badRequest().body(APIResponse.builder().message(ex.getMessage()).build());
  }
}
