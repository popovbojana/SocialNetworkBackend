FROM amazoncorretto:21
EXPOSE 8080
ARG JAR_FILE=target/socialnetwork-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
VOLUME /usr/data
ENTRYPOINT ["java","-jar","/app.jar"]