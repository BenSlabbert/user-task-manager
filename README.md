# Requirements

* Docker
* Running MySQL instance with empty schema and valid credentials

## Build

### Verify

`./mvnw clean verify`

### Test

`./mvnw clean test`

### Build Docker Image

`./docker-build.sh`

#### Example Run

`docker container run --rm --net="host" -e "DB_USERNAME=root" -e "DB_PASSWORD=root" -e "DB_URL=jdbc:mysql://localhost:3306/manager" user-task-manager/manager`

* `--net=host` connect to locally running db instance
* `DB_USERNAME`, `DB_PASSWORD`, `DB_URL` are all required fields
 
#### Run With Debug Logging
 
set the active profile: `-e "spring.profiles.active=dev"`

#### Set custom Port
 
`-e "server.port=8082"`

## DB migration

### See Updates

`./liquibase-show-update-sql.sh`

### Apply Updates

`./liquibase-update.sh`

### Roll Back Changes

`./liquibase-rollback-count.sh` (specify number of changes to roll back)
