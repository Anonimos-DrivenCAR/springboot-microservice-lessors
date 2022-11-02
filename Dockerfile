FROM openjdk:18.0.2.1-jdk

WORKDIR /app

COPY ./target/springboot-microservice-lessors-0.0.1-SNAPSHOT.jar .

EXPOSE 8003

ENTRYPOINT ["java", "-jar","springboot-microservice-lessors-0.0.1-SNAPSHOT.jar"]