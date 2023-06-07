FROM eclipse-temurin:17.0.7_7-jre-alpine

RUN mkdir -p /home/app

WORKDIR /home/app

COPY target/overengineered-todo-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./app.jar"]