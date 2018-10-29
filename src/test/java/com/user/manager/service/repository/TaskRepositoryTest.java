package com.user.manager.service.repository;

import com.user.manager.config.TestAppConfig;
import com.user.manager.entity.Task;
import com.user.manager.entity.TaskStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@ActiveProfiles("test")
@Transactional
public class TaskRepositoryTest {

  @Autowired private TaskRepository taskRepository;

  private Calendar cal = new GregorianCalendar(2018, 9, 10, 12, 0, 0);

  @Before
  public void setUp() {
    taskRepository.deleteAll();

    taskRepository.save(Task.builder().dateTime(cal.getTime()).build());

    cal.add(Calendar.HOUR_OF_DAY, 1);

    taskRepository.save(Task.builder().dateTime(cal.getTime()).status(TaskStatus.DONE).build());
  }

  @Test
  public void findAllByStatusEqualsAndDateTimeLessThanEqualTest_taskFound() {

    List<Task> all =
        taskRepository.findAllByStatusEqualsAndDateTimeLessThanEqual(
            TaskStatus.PENDING, cal.getTime());

    assertNotNull(all);
    assertEquals(1, all.size());
    assertEquals(TaskStatus.PENDING, all.get(0).getStatus());
  }

  @Test
  public void findAllByStatusEqualsAndDateTimeLessThanEqualTest_taskNotFound() {

    GregorianCalendar gregorianCalendar = new GregorianCalendar(2018, 9, 10, 11, 0, 0);

    List<Task> all =
        taskRepository.findAllByStatusEqualsAndDateTimeLessThanEqual(
            TaskStatus.PENDING, gregorianCalendar.getTime());

    assertNotNull(all);
    assertEquals(0, all.size());
  }
}
