package com.user.manager.service;

import com.user.manager.entity.User;

import java.util.List;

public interface UserService {

  User getUser(long id);

  User createUser(User userRequest);

  List<User> getAllUsers();

  User updateUser(long id, User updates);

  User updateUser(User existingUser);
}
