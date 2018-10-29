package com.user.manager.api.validator;

import com.user.manager.config.TestAppConfig;
import com.user.manager.entity.Task;
import com.user.manager.entity.User;
import com.user.manager.response.APIResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@ActiveProfiles("test")
public class UserApiValidatorTest {

  @Mock private ProceedingJoinPoint pjp;

  private UserApiValidator validator;

  @Before
  public void setUp() {
    validator = new UserApiValidator();
  }

  @Test
  public void createUserValidator_badResponseTest() throws Throwable {

    when(pjp.getArgs()).thenReturn(new Object[] {new User()});

    ResponseEntity<APIResponse> resp =
        (ResponseEntity<APIResponse>) validator.createUserValidator(pjp);

    assertNotNull(resp);

    assertEquals(400, resp.getStatusCodeValue());
    assertEquals("Must provide: user_name, first_name and last_name", resp.getBody().getMessage());

    verify(pjp, times(0)).proceed();
  }

  @Test
  public void createUserValidator_validResponseTest() throws Throwable {

    when(pjp.getArgs())
        .thenReturn(
            new Object[] {User.builder().userName("u1").firstName("'f1").lastName("l1").build()});

    validator.createUserValidator(pjp);

    verify(pjp, times(1)).proceed();
  }

  @Test
  public void updateUserValidator_badResponseTest() throws Throwable {

    when(pjp.getArgs()).thenReturn(new Object[] {new User()});

    ResponseEntity<APIResponse> resp =
        (ResponseEntity<APIResponse>) validator.updateUserValidator(pjp);

    assertNotNull(resp);

    assertEquals(400, resp.getStatusCodeValue());
    assertEquals(
        "Must provide at least one field: first_name and last_name", resp.getBody().getMessage());

    verify(pjp, times(0)).proceed();
  }

  @Test
  public void updateUserValidator_validResponseTest() throws Throwable {

    when(pjp.getArgs()).thenReturn(new Object[] {User.builder().firstName("f1").build()});

    validator.updateUserValidator(pjp);

    verify(pjp, times(1)).proceed();
  }

  @Test
  public void createTaskValidator_badResponseTest() throws Throwable {

    when(pjp.getArgs()).thenReturn(new Object[] {new Task()});

    ResponseEntity<APIResponse> resp =
        (ResponseEntity<APIResponse>) validator.createTaskValidator(pjp);

    assertNotNull(resp);

    assertEquals(400, resp.getStatusCodeValue());
    assertEquals("Must provide: name, description and date_time", resp.getBody().getMessage());

    verify(pjp, times(0)).proceed();
  }

  @Test
  public void createTaskValidator_validResponseTest() throws Throwable {

    when(pjp.getArgs())
        .thenReturn(
            new Object[] {
              Task.builder().name("t1").description("d1").dateTime(new Date()).build()
            });

    validator.createTaskValidator(pjp);

    verify(pjp, times(1)).proceed();
  }

  @Test
  public void updateTaskValidator_badResponseTest() throws Throwable {

    when(pjp.getArgs()).thenReturn(new Object[] {new Task()});

    ResponseEntity<APIResponse> resp =
        (ResponseEntity<APIResponse>) validator.updateTaskValidator(pjp);

    assertNotNull(resp);

    assertEquals(400, resp.getStatusCodeValue());
    assertEquals("Must provide: name", resp.getBody().getMessage());

    verify(pjp, times(0)).proceed();
  }

  @Test
  public void updateTaskValidator_validResponseTest() throws Throwable {

    when(pjp.getArgs()).thenReturn(new Object[] {Task.builder().name("t1").build()});

    validator.updateTaskValidator(pjp);

    verify(pjp, times(1)).proceed();
  }
}
