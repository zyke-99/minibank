# **MiniBank Application**

This document provides detailed instructions on how to set up and run the application.

## **Running the Application**

Firstly, clone the repository:

    git clone https://github.com/your-repo/minibank.git

### **1. Via IntelliJ IDEA**
Import the Project into IntelliJ IDEA:

Open IntelliJ IDEA.
Select File -> Open and choose the project directory.
Ensure the project uses the correct SDK (Java 17).


Run the Application:

Locate the main class: com.zyke.minibank.MinibankApplication.
Right-click and select Run MinibankApplication.
Access the Application:

The application should be running on http://localhost:8080.
### **2. With Maven**

Navigate via terminal to the cloned repository directory.

a. For Linux/macOS systems run the following to start the application:

    ./mvnw spring-boot:run


b. For Windows systems run the following:

    mvnw.cmd spring-boot:run


The application should be running on http://localhost:8080.
### **3. Using Docker**
Use Maven to clean and package the application into a JAR file:

    ./mvnw clean package

Run the following to build the Docker image:

    docker build -t minibank .

Afterwards you can run a container of that image via the following command:

    docker run --name minibank -p 8080:8080 minibank

## **Additional info**

### **Postman collection**

The repository contains a postman collection as a .json file, which can be imported into postman.

    /postman/minibank.postman_collection.json

### **H2 Database console**
Currently Minibank runs with an in-memory H2 database, the console should be accessible via browser:


http://localhost:8080/h2-console

Standard h2 credentials are used

    username: sa
    password: password