package com.user.manager.job;

import com.user.manager.entity.Task;
import com.user.manager.entity.TaskStatus;
import com.user.manager.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional
public class UpdateTaskJob {

  private static final Logger LOG = LoggerFactory.getLogger(UpdateTaskJob.class);

  private final TaskService taskService;

  public UpdateTaskJob(TaskService taskService) {
    this.taskService = taskService;
  }

  @Scheduled(cron = "0 0/1 * * * *")
  public void job() {

    LOG.debug("Starting update task job!");

    List<Task> allTasks = taskService.getAllPendingTasksBeforeDate(new Date());

    for (Task task : allTasks) {

      LOG.info("Updating task to done: {}", task);

      task.setStatus(TaskStatus.DONE);
      taskService.updateTask(task);
    }

    LOG.debug("Finished update task job!");
  }
}
