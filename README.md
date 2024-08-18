# Spring Cloud Kubernetes Leader Election Example
This project contains an example of how to use leader selection in a Kubernetes cluster, using only the Kubernetes API, through a Spring Boot compatible library, by [fabric8](https://fabric8.io/).

## Purpose and motivations
Currently, the microservices architecture is fully extended and is one of the most widely used by all companies to implement their solutions, although older ones such as the monolithic are still present for certain cases where they are more appropriate and other, newer ones, such as serverless are beginning to be more recurrent. The most natural thing to do is to deploy these microservices in container-based environments, such as Kubernetes, which facilitate the deployment and maintenance of our applications. A very important thing to keep in mind when developing our microservices is high availability, i.e. ensuring that the application can process a high number of requests without crashing or failing. In Kubernetes, to solve this, replicas are used; that is, several instances of the same microservice are created and requests are redirected to one replica or another, depending on the load and the configuration that has been made. 

For example, nothing should be stored in the microservice's own memory, because if the same user makes two requests, Kubernetes cannot ensure that both requests are processed in the same replica. Another problem, and the one explained in this example, is when our application needs to process scheduled tasks from time to time. By having several replicas, using the classic techniques, each replica would execute the same task at the same time, when, in fact, we require it to be executed only once. For these cases, we usually use leader selection solutions, such as Zookeeper, or we implement it manually using a key-value database, such as Redis.

However, in many cases, it is quite advisable to take advantage of what the existing infrastructure offers, as setting up solutions like Zookeeper can lead to additional costs for the infrastructure, or implementing custom solutions, such as using Redis, can complicate the development process. 

This project aims to show a simple example of how to solve leader selection in Spring Boot applications, making use of the Kubernetes environment, all implemented with the ‘org.springframework.cloud:spring-cloud-kubernetes-fabric8-leader’ library, which is a simple solution and leverages Kubernetes infrastructure.

## How it works
This example used the fabric8 leader library, which uses the Kubernetes API client to interact with kubernetes cluster. The resource used by the library to manage leader election are the ConfigMaps; a ConfigMap is an object stored in Kubernetes cluster and contains multiple keys and values associated to that keys, so allows to store information in memory. The fabric8 library leverages Kubernetes' ConfigMaps to store the name of the leader, so, when an instance check if exists any leader and the ConfigMap is set with anyone, the instance actuate as a non-leader instance.

## Requirements

### Build requirements
This project is based on SpringBoot, so it requires a JDK installed on system where will be builded; t
the required version is 21. Gradle is not needed to be installed, because this project has a wrapper to build project without installation.

### Testing requirements
This example uses a Kubernetes feature, and is designed to be tested with Minikube, which is a tool that allows you to run a local Kubernetes cluster on a virtual machine, making it easier to develop and test Kubernetes applications locally. So, you need to install it before to test the application. This is the official page with instructions to install Minikube in all platforms: [how to install minikube](https://minikube.sigs.k8s.io/docs/start/?arch=%2Fmacos%2Farm64%2Fstable%2Fbinary+download).

To install Minikube, also Docker is required, because Minikube uses it to deploy the cluster. Also, to deploy this application on Minikube, is required kubectl tool to interact with cluster. This is the official page of Kubernetes with instructions to install kubectl on different operative systems: [how to install kubectl](https://kubernetes.io/docs/tasks/tools/).

## Project structure
This repository has a Spring Boot project structure, with dependency management based on Gradle. The files and folders in project are:

* gradle: Folder with a Gradle wrapper to build project without Gradle installed on system
* src: Folder with all Java project code and resources
* .gitignore: Git ignore file to don't track some files
* build.gradle: Gradle project configuration, used to define some tasks and project dependencies
* deploy.bat: Windows terminal script to automatize build and deploy on Minikube
* deploy.sh: Bash script to automatize build and deploy on Minikube
* deployment.yml: YAML file with all objects to be deployed on Minikube for local testing
* gradlew: Script for Linux and MacOS to run Gradle wrapper
* gradlew.bat: Script for Windows to run Gradle wrapper
* settings.gradle: Gradle project's settings file. In this case, only contains project name

## How to test application
The application testing is automatized in deploy.sh and deploy.bat files, you can use first one if you're using Linux or MacOS system, or second one if you're using Windows. These scripts has this steps:

1. First, set some environment variables to use Minikube's Docker environment instead of local Docker environment. This is needed because our application must be visible by Minikube system, so, the image of our application must be registered inside Minikube local registry.
2. Then, builds the SpringBoot application and generates a Docker image using SpringBoot's custom gradle task. This task creates an image and push it to local registry automatically.
3. Applies the deployment which creates instances of our application. Also generates another objects that the application needs, like roles, role bindings, service accounts,...