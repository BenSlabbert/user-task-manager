#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "Must specify number of changes to roll back"
    exit 1
fi

COUNT=$1

./liquibase/3.5.3/liquibase rollbackCount ${COUNT}
