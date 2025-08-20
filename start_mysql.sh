#!/usr/bin/env bash

# Variables
CONTAINER_NAME="mysql-db"
MYSQL_ROOT_PASSWORD=""
MYSQL_DATABASE="CH02"
MYSQL_USER="root"
MYSQL_PASSWORD=""
HOST_PORT=3306


#VOLUME_PATH="$HOME/podman-mysql-data"

# Create volume directory if not exists
#mkdir -p "$VOLUME_PATH"

# Run MySQL container
podman run -d \
  --name $CONTAINER_NAME \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -e MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD \
  -e MYSQL_DATABASE=$MYSQL_DATABASE \
  -p $HOST_PORT:3306 \
  docker.io/library/mysql:8.0
#  -e MYSQL_USER=$MYSQL_USER \
#  -e MYSQL_PASSWORD=$MYSQL_PASSWORD \
#  -v $VOLUME_PATH:/var/lib/mysql:Z \
