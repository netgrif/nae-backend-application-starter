FROM eclipse-temurin:21-jre
MAINTAINER Netgrif <devops@netgrif.com>

WORKDIR /app

COPY target/example.jar ./app.jar
COPY src/main/resources ./src/main/resources

RUN mkdir -p storage log

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
