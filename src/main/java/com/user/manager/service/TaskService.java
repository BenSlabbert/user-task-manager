package com.user.manager.service;

import com.user.manager.entity.Task;
import com.user.manager.entity.User;

import java.util.Date;
import java.util.List;

public interface TaskService {

  List<Task> getTasksForUserId(long userId);

  List<Task> getAllPendingTasksBeforeDate(Date date);

  Task getSingleTaskForUserId(long userId, long taskId);

  Task updateTask(Task task);

  Task createTaskForUser(long userId, Task task);

  Task updateTaskForUser(long userId, long taskId, Task taskUpdates);

  User removeTaskForUser(long userId, long taskId);
}
