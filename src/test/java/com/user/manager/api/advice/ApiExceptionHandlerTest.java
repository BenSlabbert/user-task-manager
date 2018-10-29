package com.user.manager.api.advice;

import com.user.manager.api.exception.APIException;
import com.user.manager.response.APIResponse;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ApiExceptionHandlerTest {

  @Test
  public void exceptionTest() {

    ApiExceptionHandler handler = new ApiExceptionHandler();

    ResponseEntity<APIResponse> responseEntity =
        handler.handleAPIException(new APIException("test"));

    assertNotNull(responseEntity);

    assertEquals(400, responseEntity.getStatusCodeValue());

    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getMessage());
    assertEquals("test", responseEntity.getBody().getMessage());
  }
}
