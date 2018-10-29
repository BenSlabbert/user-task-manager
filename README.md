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

This builds a docker images tagged as `user-task-manager/manager:latest`

## Example Run

`docker container run --rm --net="host" -e "DB_USERNAME=root" -e "DB_PASSWORD=root" -e "DB_URL=jdbc:mysql://localhost:3306/manager" user-task-manager/manager`

* `--net=host` connect to locally running db instance
* `DB_USERNAME`, `DB_PASSWORD`, `DB_URL` are all required fields
 
#### Debug Logging
 
set the active profile: `-e "spring.profiles.active=dev"`

#### Custom Port

`-e "server.port=8082"`

## DB migration

Liquibase change file = `liquibase.sql`

Note: it is required to run the liquibase `update` script before starting the application.

### Validation and Exception Handling

If a user or task cannot be found an `com.user.manager.api.exception.APIException` is thrown.

These exceptions are handled in: `com.user.manager.api.advice.ApiExceptionHandler`.

Validation is handled in: `com.user.manager.api.validator.UserApiValidator`.

#### Creating a User

Required fields: `user_name`, `first_name`, `last_name`.

#### Updating a User

Required fields, at least one of: `user_name`, `first_name`, `last_name`.

#### Creating a Task

Required fields: `name`, `description`, `date_time`.

#### Updating a Task

Required field: `name`.

## Examples with cURL

### Create User

Request: `curl -i -H "Content-Type: application/json" -X POST -d '{"username":"jsmith","first_name" : "John", "last_name" : "Smith"}' http://localhost:8080/api/user`

Response: 

    {
        "data" : 
            { 
                "id":5,    
                tasks":[],
                "username":"jsmith",
                "first_name":"John",
                "last_name":"Smith"
            }
    }

### Update User

`curl -i -H "Content-Type: application/json" -X PUT -d '{"first_name" : "John", "last_name" : "Doe"}' http://localhost:8080/api/user/{id}`

Response: 

    {
        "data" : 
        {
            "id":5,
            "tasks":[],
            "username":"jsmith",
            "first_name":"John",
            "last_name":"Doe"
        }
    }

### List All Users

Request: `curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/api/user`

Response: 

    {
        "data": 
        [
            {
                "id":5,
                "tasks":[],
                "username":"jsmith",
                "first_name":"John",
                "last_name":"Smith"
            }
        ]
    }

### Get User Info

Request: `curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/api/user/{id}`
 
Response: 

    {
        "data":
            {
                "id":5,
                "tasks":[],
                "username":"jsmith",
                "first_name":"John",
                "last_name":"Smith"
            }
    }

### Create Task

Request: `curl -i -H "Content-Type: application/json" -X POST -d '{"name":"My task","description" : "Description of task", "date_time" : "2016-05-25 14:25:00"}' http://localhost:8080/api/user/{user_id}/task`

Response: 

    {
        "data":
            {
                "id":5,
                "tasks":
                    [
                        {
                            "id":139,
                            "name":"My task",
                            "description":"Description of task",
                            "status":"PENDING",
                            "date_time":"2016-05-25 02:25:00"
                        }
                    ],
                "username":"jsmith",
                "first_name":"John",
                "last_name":"Smith"
            }
    }

### Update Task

Request: `curl -i -H "Content-Type: application/json" -X PUT -d '{"name":"My updated task"}' http://localhost:8080/api/user/{user_id}/task/{task_id}`

Response:

    {
        "data":
            {
                "id":5,
                "tasks":
                    [
                        {
                            "id":140,
                            "name":"My updated task",
                            "description":"Description of task",
                            "status":"DONE",
                            "date_time":"2016-05-25 02:25:00"
                        }
                    ],
                "username":"jsmith",
                "first_name":"John",
                "last_name":"Smith"
            }
    }

### Delete Task

Request: `curl -i -H "Content-Type: application/json" -X DELETE http://localhost:8080/api/user/{user_id}/task/{task_id}`

Response: 

    {
        "data":"Task deleted!"
    }

### Get Task Info

Request: `curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/api/user/{user_id}/task/{task_id}`

Response: 

    {
        "data":
            {
                "id":139,
                "name":"My updated task",
                "description":"Description of task",
                "status":"DONE",
                "date_time":"2016-05-25 02:25:00"
            }
    }

### List Tasks for User

Request: `curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/api/user/{user_id}/task`

Response: 

    {
        "data":
            [
                {
                    "id":140,
                    "name":"My task",
                    "description":"Description of task",
                    "status":"PENDING",
                    "date_time":"2016-05-25 02:25:00"
                }
            ]
    }
