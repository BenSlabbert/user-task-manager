package com.user.manager.service;

import com.user.manager.api.exception.APIException;
import com.user.manager.config.TestAppConfig;
import com.user.manager.entity.Task;
import com.user.manager.entity.User;
import com.user.manager.service.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@ActiveProfiles("test")
public class TaskServiceImplTest {

  @MockBean private TaskRepository taskRepository;
  @MockBean private UserService userService;

  private TaskService taskService;

  @Before
  public void setUp() {
    taskService = new TaskServiceImpl(taskRepository, userService);
  }

  @Test
  public void getTasksForUserIdTest() {

    Task task = Task.builder().id(1L).name("task1").build();

    User user = User.builder().id(1L).tasks(Collections.singletonList(task)).build();

    when(userService.getUser(1L)).thenReturn(user);

    List<Task> taskList = taskService.getTasksForUserId(1L);

    assertNotNull(taskList);
    assertEquals(1, taskList.size());
    assertSame(task, taskList.get(0));

    verify(userService, times(1)).getUser(1L);
  }

  @Test
  public void getSingleTaskForUserIdTest() {

    Task task = Task.builder().id(1L).name("task1").build();

    User user = User.builder().id(1L).tasks(Collections.singletonList(task)).build();

    when(userService.getUser(1L)).thenReturn(user);

    Task taskForUserId = taskService.getSingleTaskForUserId(1L, 1L);

    assertNotNull(taskForUserId);
    assertSame(task, taskForUserId);

    verify(userService, times(1)).getUser(1L);
  }

  @Test
  public void createTaskForUserTest() {

    Task task = Task.builder().id(1L).name("task1").build();

    User user = User.builder().id(1L).tasks(new ArrayList<>()).build();

    when(userService.getUser(1L)).thenReturn(user);

    user.getTasks().add(task);

    when(userService.updateUser(user)).thenReturn(user);

    Task createdTask = taskService.createTaskForUser(1L, task);

    assertNotNull(createdTask);
    assertSame(task, createdTask);

    verify(userService, times(1)).updateUser(user);
  }

  @Test
  public void updateTaskForUserTest() {

    Task task = Task.builder().id(1L).name("task1").build();

    User user = User.builder().id(1L).tasks(Collections.singletonList(task)).build();

    when(userService.getUser(1L)).thenReturn(user);

    taskService.updateTaskForUser(
        1L,
        1L,
        Task.builder().name("new name").description("new desc").dateTime(new Date(1L)).build());

    Task t = user.getTasks().get(0);
    t.setName("new name");
    t.setDescription("new desc");
    t.setDateTime(new Date(1));

    verify(userService, times(1)).updateUser(user);
  }

  @Test
  public void removeTaskForUserTest() {

    Task task = Task.builder().id(1L).name("task1").build();

    User user = User.builder().id(1L).tasks(Collections.singletonList(task)).build();

    when(userService.getUser(1L)).thenReturn(user);

    taskService.removeTaskForUser(1L, 1L);

    user.setTasks(Collections.emptyList());

    verify(userService, times(1)).updateUser(user);
  }

  @Test(expected = APIException.class)
  public void getTaskTest_incorrectTaskId() {

    ArrayList<Task> tasks = new ArrayList<>();

    ReflectionTestUtils.invokeMethod(taskService, "getTask", 1L, 1L, tasks);
  }

  @Test
  public void getTaskTest() {

    ArrayList<Task> tasks = new ArrayList<>();
    Task task = Task.builder().id(2L).build();
    tasks.add(task);

    Task t = ReflectionTestUtils.invokeMethod(taskService, "getTask", 1L, 2L, tasks);

    assertNotNull(t);
    assertSame(task, t);
  }
}
