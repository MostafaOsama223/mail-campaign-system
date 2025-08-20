FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

COPY gradlew gradlew
COPY gradle/wrapper gradle/wrapper

RUN chmod +x gradlew
RUN ./gradlew --no-daemon --version

COPY settings.gradle* build.gradle* ./

RUN ./gradlew --no-daemon dependencies || true

COPY . .
RUN ./gradlew --no-daemon -x test bootJar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
