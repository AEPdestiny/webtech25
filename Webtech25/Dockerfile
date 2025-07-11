FROM gradle:jdk21-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
ARG DATABASE_password
ARG DATABASE_URL
ARG DATABASE_username
RUN gradle build --no-daemon

FROM eclipse-temurin:21-jdk-jammy
COPY --from=build /Users/ibrah/IdeaProjects/webtech25/Webtech25/Webtech25/build/libs/Webtech25-0.0.1-SNAPSHOT-plain.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
