FROM openjdk:11.0.14-jre-slim
EXPOSE 8080
WORKDIR .
COPY ./target/demo-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar","./demo-0.0.1-SNAPSHOT.jar"]