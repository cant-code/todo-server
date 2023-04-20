FROM openjdk:17-alpine

RUN mkdir -p /home/app

WORKDIR /home/app

COPY target/overengineered-todo-server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "./app.jar"]