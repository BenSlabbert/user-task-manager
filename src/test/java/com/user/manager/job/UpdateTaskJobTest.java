package com.user.manager.job;

import com.user.manager.config.TestAppConfig;
import com.user.manager.entity.Task;
import com.user.manager.entity.TaskStatus;
import com.user.manager.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@ActiveProfiles("test")
public class UpdateTaskJobTest {

  @MockBean private TaskService taskService;

  private UpdateTaskJob updateTaskJob;

  @Before
  public void setUp() {
    updateTaskJob = new UpdateTaskJob(taskService);
  }

  @Test
  public void jobTest_updatePendingTasks() {

    ArrayList<Task> tasks = new ArrayList<>();

    tasks.add(
        Task.builder()
            .name("name")
            .description("description")
            .status(TaskStatus.PENDING)
            .dateTime(new Date(1L))
            .build());

    when(taskService.getAllPendingTasksBeforeDate(any())).thenReturn(tasks);

    updateTaskJob.job();

    verify(taskService, times(1))
        .updateTask(
            Task.builder()
                .name("name")
                .description("description")
                .status(TaskStatus.DONE)
                .dateTime(new Date(1L))
                .build());
  }

  @Test
  public void jobTest_updatePendingTasks_noResults() {

    when(taskService.getAllPendingTasksBeforeDate(any())).thenReturn(new ArrayList<>());

    updateTaskJob.job();

    verify(taskService, times(0)).updateTask(any());
  }
}
