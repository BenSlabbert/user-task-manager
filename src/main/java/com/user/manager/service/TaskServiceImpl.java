package com.user.manager.service;

import com.user.manager.api.exception.APIException;
import com.user.manager.entity.Task;
import com.user.manager.entity.TaskStatus;
import com.user.manager.entity.User;
import com.user.manager.service.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

  private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

  private final TaskRepository taskRepository;
  private final UserService userService;

  public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
    this.taskRepository = taskRepository;
    this.userService = userService;
  }

  @Override
  public List<Task> getTasksForUserId(long userId) {

    LOG.debug("Getting user for id: {}", userId);

    return userService.getUser(userId).getTasks();
  }

  @Override
  public List<Task> getAllPendingTasksBeforeDate(Date date) {

    LOG.debug("Getting pending tasks before date: {}", date);

    return taskRepository.findAllByStatusEqualsAndDateTimeLessThanEqual(TaskStatus.PENDING, date);
  }

  @Override
  public Task getSingleTaskForUserId(long userId, long taskId) {

    LOG.debug("Getting taskId: {} for userId: {}", taskId, userId);

    User user = userService.getUser(userId);

    return getTask(userId, taskId, user.getTasks());
  }

  @Override
  public Task updateTask(Task task) {

    LOG.debug("Updating task: {}", task);

    return taskRepository.save(task);
  }

  @Override
  public User createTaskForUser(long userId, Task task) {

    LOG.debug("Adding task: {} to userId: {}", task, userId);

    User user = userService.getUser(userId);

    task.setStatus(TaskStatus.PENDING);

    user.getTasks().add(task);

    return userService.updateUser(user);
  }

  @Override
  public User updateTaskForUser(long userId, long taskId, Task taskUpdates) {

    LOG.debug("Updating taskId: {} for userId: {} with task: {}", taskId, userId, taskUpdates);

    User user = userService.getUser(userId);

    Task task = getTask(userId, taskId, user.getTasks());

    task.setName(taskUpdates.getName());

    return userService.updateUser(user);
  }

  private Task getTask(long userId, long taskId, List<Task> tasks) {

    Optional<Task> optionalTask = tasks.stream().filter(f -> f.getId() == taskId).findFirst();

    if (!optionalTask.isPresent()) {
      LOG.warn("No task found for id: {} userId: {}", taskId, userId);
      throw new APIException("No Task found for user!");
    }

    return optionalTask.get();
  }

  @Override
  public User removeTaskForUser(long userId, long taskId) {

    LOG.debug("Getting taskId: {} for userId: {}", taskId, userId);

    User user = userService.getUser(userId);

    List<Task> tasks =
        user.getTasks().stream().filter(f -> f.getId() != taskId).collect(Collectors.toList());

    user.setTasks(tasks);

    return userService.updateUser(user);
  }
}
