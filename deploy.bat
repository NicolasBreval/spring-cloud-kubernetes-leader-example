@echo off

for /f "tokens=*" %%i in ('minikube docker-env --shell cmd') do %%i
gradlew bootBuildImage --imageName=leader_example
kubectl apply -f deployment.yml
