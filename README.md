# Build Badges

[![SonarCloud](https://github.com/cant-code/todo-server/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/cant-code/todo-server/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=cant-code_todo-server&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=cant-code_todo-server)

# Getting Started

### Technologies Used

* Java 17
* Spring Boot 3
* Spring Reactive
* Redis
* Docker (For testcontainers)
* Firebase Authentication

### Running on Local

* Add the Redis connection details in the application.yml file
  * spring.data.redis.host
  * spring.data.redis.port
  * spring.data.redis.username
  * spring.data.redis.password
* Activate any profiles if necessary
* Run the application

### Running on Docker

Open up command prompt/terminal

* Build the image by running `docker build -t {image-name}:{tag} .`
* Run the image by running -
  `docker run -p 8080:8080 -d {image-name}:{tag}`

Include any env variables as necessary in the run command

### Endpoints

The application runs on port 8080 by default with context `/api`

* [API Endpoint](http://localhost:8080/api/)
* [Swagger UI](http://localhost:8080/api/webjars/swagger-ui/index.html)

Management Port runs on 18080 by default

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.3/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.3/reference/htmlsingle/#using.devtools)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.0.3/reference/htmlsingle/#web.reactive)
