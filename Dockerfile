FROM openjdk:18.0.2.1-jdk as builder

WORKDIR /app/springboot-microservice-lessors

COPY ./pom.xml /app
COPY ./springboot-microservice-lessors/.mvn ./.mvn
COPY ./springboot-microservice-lessors/mvnw .
COPY ./springboot-microservice-lessors/pom.xml .

#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline
COPY ./springboot-microservice-lessors/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:18.0.2.1-jdk

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/springboot-microservice-lessors/target/springboot-microservice-lessors-0.0.1-SNAPSHOT.jar .
EXPOSE 8003

CMD ["java", "-jar","springboot-microservice-lessors-0.0.1-SNAPSHOT.jar"]