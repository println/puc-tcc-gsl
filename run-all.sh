#!/bin/bash

set -e
set -u

services=(order freight collection transportation delivery)
ports=(8761 8762 8763 8764 8765)
version=v1

for i in "${!services[@]}"; do
  echo LAUNCHING SERVICE "${services[$i]}" to port: "${ports[$i]}"
  docker run -d --name "${services[$i]}"-container -p "${ports[$i]}":8080 gsl/"${services[$i]}"-service:$version
done