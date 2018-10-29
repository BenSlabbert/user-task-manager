package com.user.manager.api.validator;

import com.user.manager.entity.Task;
import com.user.manager.entity.User;
import com.user.manager.response.APIResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class UserApiValidator {

  private static final Logger LOG = LoggerFactory.getLogger(UserApiValidator.class);

  @Around("execution(* com.user.manager.api.UserAPI.createUser(..))")
  public Object createUserValidator(ProceedingJoinPoint pjp) throws Throwable {

    User user = (User) pjp.getArgs()[0];

    if (user.getUserName() == null || user.getFirstName() == null || user.getLastName() == null) {

      LOG.warn("Bad create user object! Required args not provided!");

      return ResponseEntity.badRequest()
          .body(
              APIResponse.builder()
                  .message("Must provide: user_name, first_name and last_name")
                  .build());
    }

    return pjp.proceed();
  }

  @Around("execution(* com.user.manager.api.UserAPI.updateUser(..))")
  public Object updateUserValidator(ProceedingJoinPoint pjp) throws Throwable {

    User user = (User) pjp.getArgs()[0];

    if (user.getUserName() == null && user.getFirstName() == null && user.getLastName() == null) {

      LOG.warn("Bad update user object! All args empty!");

      return ResponseEntity.badRequest()
          .body(
              APIResponse.builder()
                  .message("Must provide at least one field: user_name, first_name and last_name")
                  .build());
    }

    return pjp.proceed();
  }

  @Around("execution(* com.user.manager.api.UserAPI.createTask(..))")
  public Object createTaskValidator(ProceedingJoinPoint pjp) throws Throwable {

    Task task = (Task) pjp.getArgs()[0];

    if (task.getName() == null || task.getDescription() == null || task.getDateTime() == null) {

      LOG.warn("Bad create user object! Required args not provided!");

      return ResponseEntity.badRequest()
          .body(
              APIResponse.builder()
                  .message("Must provide: name, description and date_time")
                  .build());
    }

    return pjp.proceed();
  }

  @Around("execution(* com.user.manager.api.UserAPI.updateTask(..))")
  public Object updateTaskValidator(ProceedingJoinPoint pjp) throws Throwable {

    Task task = (Task) pjp.getArgs()[0];

    if (task.getName() == null) {

      LOG.warn("Bad update task object! Name is empty!");

      return ResponseEntity.badRequest()
          .body(APIResponse.builder().message("Must provide: name").build());
    }

    return pjp.proceed();
  }
}
