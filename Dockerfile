# ---------- Build-Stage --------------------------
FROM gradle:jdk21-jammy AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
ARG DATABASE_password
ARG DATABASE_URL
ARG DATABASE_username
RUN gradle bootJar --no-daemon          # erzeugt build/libs/…jar

# ---------- Run-Stage ----------------------------
FROM eclipse-temurin:21-jre-jammy       # nur JRE für Runtime nötig
COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]