#!/bin/bash
eval $(minikube docker-env)
./gradlew bootBuildImage --imageName=leader_example
kubectl apply -f deployment.yml