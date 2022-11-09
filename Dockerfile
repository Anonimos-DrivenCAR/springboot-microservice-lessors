ARG MSVC_NAME=springboot-microservice-lessors

FROM openjdk:18.0.2.1-jdk as builder

ARG MSVC_NAME

WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline

COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:18.0.2.1-jdk

ARG MSVC_NAME

WORKDIR /app

RUN mkdir ./logs

ARG TARGET_FOLDER=/app/springboot-microservice-lessors/target

COPY --from=builder $TARGET_FOLDER/springboot-microservice-lessors-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8003

ENV PORT $PORT_APP

EXPOSE $PORT

CMD ["java", "-jar","springboot-microservice-lessors-0.0.1-SNAPSHOT.jar"]