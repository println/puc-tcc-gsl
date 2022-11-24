#!/bin/bash

set -e
set -u

services=(order freight collection transportation delivery)
version=v1

for i in "${!services[@]}"; do
  echo STOPING SERVICE "${services[$i]}"
  docker stop "${services[$i]}"-container
done
