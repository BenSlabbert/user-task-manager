-- liquibase formatted sql

-- changeset manager:1

create table manager.user (
  id         bigint auto_increment not null,
  version    integer,
  user_name  varchar(25),
  first_name varchar(25),
  last_name  varchar(25),
  primary key (id)
)
  engine = InnoDB;

-- rollback drop table manager.user;

-- changeset manager:2

create table manager.task (
  id          bigint auto_increment not null,
  version     integer,
  name        varchar(25),
  description varchar(25),
  date_time   datetime,
  status      varchar(25),
  primary key (id)
)
  engine = InnoDB;

-- rollback drop table manager.task;

-- changeset manager:3

create table manager.user_task (
  user_id  bigint not null,
  tasks_id bigint not null,
  primary key (user_id, tasks_id)
)
  engine = InnoDB;

alter table manager.user_task
  add constraint user_id_fk foreign key (user_id) references manager.user (id);

alter table manager.user_task
  add constraint task_id_fk foreign key (tasks_id) references manager.task (id);

-- rollback drop table manager.user_task;
