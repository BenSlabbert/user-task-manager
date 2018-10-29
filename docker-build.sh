#!/usr/bin/env bash

# creates jar package and builds docker image

echo "Building project ..."
mvn clean package -Dmaven.test.skip=true

mvn dockerfile:build
