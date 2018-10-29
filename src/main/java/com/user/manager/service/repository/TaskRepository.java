package com.user.manager.service.repository;

import com.user.manager.entity.Task;
import com.user.manager.entity.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

  List<Task> findAllByStatusEqualsAndDateTimeLessThanEqual(TaskStatus taskStatus, Date date);
}
