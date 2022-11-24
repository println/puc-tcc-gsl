#!/bin/bash

set -e
set -u

services=(order freight collection transportation delivery)
version=v1

kubectl create namespace gsl --dry-run

for i in "${!services[@]}"; do
  echo APLLYING TO "${services[$i]}" 
  kubectl apply -f  "${services[$i]}"/k8s/deployment.yml
  kubectl apply -f  "${services[$i]}"/k8s/service.yml
done
