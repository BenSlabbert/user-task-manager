package com.user.manager.service;

import com.user.manager.api.exception.APIException;
import com.user.manager.entity.User;
import com.user.manager.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User getUser(long id) {

    LOG.debug("Get user by id: {}", id);

    Optional<User> user = userRepository.findById(id);

    if (!user.isPresent()) {
      LOG.warn("No user found for id: {}", id);
      throw new APIException("No user for id: " + id);
    }

    return user.get();
  }

  @Override
  public User createUser(User userRequest) {

    LOG.debug("Create user: user: {}", userRequest);

    return userRepository.save(
        User.builder()
            .userName(userRequest.getUserName())
            .firstName(userRequest.getFirstName())
            .lastName(userRequest.getLastName())
            .build());
  }

  @Override
  public List<User> getAllUsers() {

    LOG.debug("Find all  users");

    return (List<User>) userRepository.findAll();
  }

  @Override
  public User updateUser(long id, User updates) {

    LOG.debug("Updating userId: {}, updates: {}", id, updates);

    Optional<User> user = userRepository.findById(id);

    if (!user.isPresent()) {
      LOG.warn("Unknown userId: {}", id);
      throw new APIException("Failed to find user with id: " + id);
    }

    user.get().setFirstName(updates.getFirstName());
    user.get().setLastName(updates.getLastName());

    return userRepository.save(user.get());
  }

  @Override
  public User updateUser(User existingUser) {
    return userRepository.save(existingUser);
  }
}
