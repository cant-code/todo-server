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
  * redis.host, redis.port, redis.username, redis.password
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

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.3/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.3/reference/htmlsingle/#using.devtools)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.0.3/reference/htmlsingle/#web.reactive)
