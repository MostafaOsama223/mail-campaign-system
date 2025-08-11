FROM openjdk:17-slim

COPY build/libs/mail-fanout-service-0.0.1-SNAPSHOT.jar mail-fanout-service.jar

ENTRYPOINT ["java", "-jar", "mail-fanout-service.jar"]