package com.user.manager.service;

import com.user.manager.api.exception.APIException;
import com.user.manager.config.TestAppConfig;
import com.user.manager.entity.User;
import com.user.manager.service.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@ActiveProfiles("test")
public class UserServiceImplTest {

  @MockBean private UserRepository userRepository;

  private UserService userService;

  @Before
  public void setUp() {
    userService = new UserServiceImpl(userRepository);
  }

  @Test
  public void getUserTest() {

    User userInDB =
        User.builder().id(1L).userName("uname").firstName("fname").lastName("lname").build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(userInDB));

    User user = userService.getUser(1L);

    assertNotNull(user);
    assertSame(userInDB, user);
  }

  @Test(expected = APIException.class)
  public void getUserTest_userNotFound() {

    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    userService.getUser(2L);
  }

  @Test
  public void createUserTest() {

    User userBefore = User.builder().userName("uname").firstName("fname").lastName("lname").build();

    User userAfter =
        User.builder().id(1L).userName("uname").firstName("fname").lastName("lname").build();

    when(userRepository.save(userBefore)).thenReturn(userAfter);

    User user =
        userService.createUser(
            User.builder().userName("uname").firstName("fname").lastName("lname").build());

    assertNotNull(user);
    assertEquals(userAfter.getUserName(), user.getUserName());
    assertEquals(userAfter.getLastName(), user.getLastName());
    assertEquals(userAfter.getFirstName(), user.getFirstName());
    assertEquals(userAfter.getId().longValue(), user.getId().longValue());
  }

  @Test
  public void updateUserTest() {

    User updates = User.builder().firstName("fupdated").lastName("lupdated").build();

    User userBefore =
        User.builder().id(1L).userName("uname").firstName("fname").lastName("lname").build();

    when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userBefore));

    userService.updateUser(1L, updates);

    verify(userRepository, times(1))
        .save(
            User.builder()
                .id(1L)
                .userName("uname")
                .firstName("fupdated")
                .lastName("lupdated")
                .build());
  }

  @Test(expected = APIException.class)
  public void updateUserTest_userNotFound() {

    User updates = User.builder().firstName("fupdated").lastName("lupdated").build();

    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    userService.updateUser(1L, updates);

    verify(userRepository, times(0)).save(any());
  }
}
