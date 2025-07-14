# ---------- BUILD-STAGE ----------
FROM gradle:8.7.0-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle bootJar --no-daemon

# ---------- RUNTIME-STAGE ----------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]