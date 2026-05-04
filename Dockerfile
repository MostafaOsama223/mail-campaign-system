FROM gradle:8.10.2-jdk17 AS build

WORKDIR /app

ARG SERVICE_NAME

COPY ${SERVICE_NAME}/build.gradle ${SERVICE_NAME}/settings.gradle ./${SERVICE_NAME}/
COPY ${SERVICE_NAME}/src ./${SERVICE_NAME}/src

WORKDIR /app/${SERVICE_NAME}

RUN gradle clean bootJar --no-daemon


FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

ARG SERVICE_NAME

COPY --from=build /app/${SERVICE_NAME}/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]