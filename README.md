# Description

This is a Spring Boot app which manages users and their tasks.

When a task is created it has an initial state of `PENDING`. 

Every minute the job `UpdateTaskJob` updates all tasks marked as `Pending` to `DONE` where the current time is greater and equal to the tasks' `date_time` field.

## Requirements

* [Docker](https://www.docker.com/)
* Running MySQL instance with empty schema and valid credentials
* [Liquibase](https://www.liquibase.org/) (tested with v3.5.3) + [MySQL Connector](https://mvnrepository.com/artifact/mysql/mysql-connector-java) (tested with v5.1.35)

## Build

### Verify

`./mvnw clean verify`

### Test

`./mvnw clean test`

### Executable JAR

`./mvnw clean package`

### Docker Image

`./docker-build.sh`

This builds a docker image tagged as `user-task-manager/manager:latest`

## DB migration

Liquibase change file = `liquibase.sql`

Note: it is required to run the liquibase `update` script before starting the application.

## Example Run

`docker container run --rm --net="host" -e "DB_USERNAME=root" -e "DB_PASSWORD=root" -e "DB_URL=jdbc:mysql://localhost:3306/manager" user-task-manager/manager`

* `--net=host` connect to locally running db instance
* `DB_USERNAME`, `DB_PASSWORD`, `DB_URL` are all required fields
 
#### Debug Logging
 
set the active profile: `-e "spring.profiles.active=dev"`

#### Custom Port

`-e "server.port=8082"`

### Validation and Exception Handling

If a user or task cannot be found an `com.user.manager.api.exception.APIException` is thrown.

These exceptions are handled in: `com.user.manager.api.advice.ApiExceptionHandler`.

Validation is handled in: `com.user.manager.api.validator.UserApiValidator`.

#### Creating a User

Required fields: `user_name`, `first_name`, `last_name`.

#### Updating a User

Required fields, at least one of: `first_name`, `last_name`. `user_name` is ignored.

#### Creating a Task

Required fields: `name`, `description`, `date_time`.

#### Updating a Task

Required field: `name`.

## Postman

* [Collection](https://www.getpostman.com/collections/8d7d771e3f118c92ac3a)

* [Documentation](https://documenter.getpostman.com/view/1229795/RzZ1sNvt)
