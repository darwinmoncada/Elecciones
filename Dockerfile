FROM openjdk:17
EXPOSE 8115
ADD ./target/votaciones-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]