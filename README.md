# Dynamic TPS Allocation to POD using pod count calculation with a fixed targeted TPS value

This project dynamically allocates TPS (Transactions Per Second) to Kubernetes Pods by calculating the required Pod count based on a fixed targeted TPS value. It ensures efficient scaling and load management within a Kubernetes cluster.

---

## Prerequisites

Before running this project, ensure you have the following installed and configured:

**_Note: All this steps are for linux kernel based os_**

1. **Docker**  
   Required for building and managing container images.
2. **kubectl**  
   Command-line tool to interact with Kubernetes clusters.
3. **Minikube** (Running in Docker container)  
   A local Kubernetes environment for testing.
4. **A Preferred Editor**  
   Recommended editors are IntelliJ IDEA or Visual Studio Code.

---
## Installation Procedures

### **Docker Installation**
1. Visit the [Docker website](https://docs.docker.com/engine/install/ubuntu/) and download Docker for your operating system.
2. Follow the installation instructions provided for your OS.
3. Verify the installation:
   ```bash
   docker --version

### **kubectl Installation**
1. Visit the [kubectl installation in linux](https://docs.docker.com/engine/install/ubuntu/) and follow the steps to install.
2. Verify the installation:
   ```bash
    kubectl version --client


### **Minikube Installation**
1. Visit the [minikube installation in linux](https://minikube.sigs.k8s.io/docs/start/?arch=%2Flinux%2Fx86-64%2Fstable%2Fbinary+download) and follow the steps to install.
2. Verify the installation:
   ```bash
    minikube version
3. Set Minikube to use Docker as the driver (By default if not then applicable)
   ```bash
    minikube config set driver docker

## How to Run the Application

Follow these steps to deploy and run the application in a Minikube environment.

### Step 1: Clone the Repository

    git clone <repository-url>
    cd <repository-folder>

### Step 2: Build The Docker Image 
1. Navigate to the project directory containing the Dockerfile.
2. Build the Docker image for the application:
   ```bash
   docker build -t dynamic-tps-app:v1 .

### Step 3: Start Minikube and Load the Docker Image
1. Start Minikube: 
    ```bash
   minikube start
   
2. Load the docker image into minikube:
   ```bash
   minikube image load dynamic-tps-app:v1

3. Verify the image is loaded in this list:
   ```bash
   minikube image ls

### Step 4: Deploy the application
1. Deploy the application
    ```bash
     #deploy  the app
     kubectl create -f dynamic-tps-app-deploy.yaml
     #deploy  redis
     kubectl create -f dynamic-tps-redis-deploy.yaml
     #deploy  test-app
     kubectl create -f test-app-deploy.yaml
   
     #deploy  app service (nodeport)
     kubectl create -f dynamic-tps-app-service.yaml
     #deploy  redis service (cluster ipo)
     kubectl create -f dynamic-tps-redis-service.yaml
     #deploy  test-app-service
     kubectl create -f test-app-service.yaml

2. Verify that the Pods and Services are running:
    ```bash
    kubectl get pods
    kubectl get services

3. Get the URL for dynamic-tps-app-service
    ```bash
    minikube service dynamic-tps-app-service --url
    #output should be: http://192.168.49.2:30004

4. Access the app using curl or browser locally:
- curl:
  - curl -X GET http://192.168.49.2:30004/processor/pods | jq
  - curl -X GET http://192.168.49.2:30004/processor/count | jq
  - curl -X GET http://192.168.49.2:30004/processor/call-test-module | jq


**_Note:_** The application can be tested using scaling the deployments, deleting pods, rolling update and other ways. 

