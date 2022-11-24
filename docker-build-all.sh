#!/bin/bash

set -e
set -u

services=(order freight collection transportation delivery)
version=v1

for service in ${services[@]}; do
   echo BUILDING $service...
   docker build -t gsl/$service-service:$version -f $service/DOCKERFILE ./$service
done
