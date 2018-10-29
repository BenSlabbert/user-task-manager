package com.user.manager.api;

import com.user.manager.entity.Task;
import com.user.manager.entity.User;
import com.user.manager.response.APIResponse;
import com.user.manager.service.TaskService;
import com.user.manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@Scope("request")
public class UserAPI {

  private static final Logger LOG = LoggerFactory.getLogger(UserAPI.class);
  private static final String JSON = APPLICATION_JSON_VALUE;

  private final UserService userService;
  private final TaskService taskService;

  public UserAPI(UserService userService, TaskService taskService) {
    this.userService = userService;
    this.taskService = taskService;
  }

  private ResponseEntity<APIResponse> buildResponse(Object data) {
    return ResponseEntity.ok(APIResponse.builder().data(data).build());
  }

  @GetMapping(path = "/user", produces = JSON)
  public ResponseEntity<APIResponse> getAllUsers() {

    LOG.debug("Getting all users");

    return buildResponse(userService.getAllUsers());
  }

  @GetMapping(path = "/user/{id}", produces = JSON)
  public ResponseEntity<APIResponse> getUserById(@PathVariable Long id) {

    LOG.debug("Getting user by id: {}", id);

    return buildResponse(userService.getUser(id));
  }

  @PostMapping(path = "/user", produces = JSON, consumes = JSON)
  public ResponseEntity<APIResponse> createUser(@RequestBody User userRequest) {

    LOG.debug("Creating user: {}", userRequest);

    return buildResponse(userService.createUser(userRequest));
  }

  @PutMapping(path = "/user/{id}", produces = JSON, consumes = JSON)
  public ResponseEntity<APIResponse> updateUser(
      @RequestBody User updateRequest, @PathVariable long id) {

    LOG.debug("Updating user id: {}", id);

    return buildResponse(userService.updateUser(id, updateRequest));
  }

  @PostMapping(path = "/user/{user_id}/task", produces = JSON, consumes = JSON)
  public ResponseEntity<APIResponse> createTask(
      @RequestBody Task task, @PathVariable(name = "user_id") long userId) {

    LOG.debug("Creating task for user id: {}", userId);

    return buildResponse(taskService.createTaskForUser(userId, task));
  }

  @PutMapping(path = "/user/{user_id}/task/{task_id}", produces = JSON, consumes = JSON)
  public ResponseEntity<APIResponse> updateTask(
      @RequestBody Task task,
      @PathVariable(name = "user_id") long userId,
      @PathVariable(name = "task_id") long taskId) {

    LOG.debug("Updating taskId: {} for userId: {}", taskId, userId);

    return buildResponse(taskService.updateTaskForUser(userId, taskId, task));
  }

  @DeleteMapping(path = "/user/{user_id}/task/{task_id}", produces = JSON)
  public ResponseEntity<APIResponse> deleteTask(
      @PathVariable(name = "user_id") long userId, @PathVariable(name = "task_id") long taskId) {

    LOG.debug("Updating taskId: {} for userId: {}", taskId, userId);

    taskService.removeTaskForUser(userId, taskId);

    return buildResponse("Task deleted!");
  }

  @GetMapping(path = "/user/{user_id}/task/{task_id}", produces = JSON)
  public ResponseEntity<APIResponse> getTask(
      @PathVariable(name = "user_id") long userId, @PathVariable(name = "task_id") long taskId) {

    LOG.debug("Getting taskId: {} for userId: {}", taskId, userId);

    return buildResponse(taskService.getSingleTaskForUserId(userId, taskId));
  }

  @GetMapping(path = "/user/{user_id}/task", produces = JSON)
  public ResponseEntity<APIResponse> getAllTasks(@PathVariable(name = "user_id") long userId) {

    LOG.debug("Getting tasks for userId: {}", userId);

    return buildResponse(taskService.getTasksForUserId(userId));
  }
}
