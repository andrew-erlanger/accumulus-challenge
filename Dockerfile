FROM gradle:7.6.0-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17.0.1-jdk-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/challenge-application.jar

ENTRYPOINT ["java", "-jar","/app/challenge-application.jar"]