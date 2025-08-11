FROM openjdk:17-slim

COPY build/libs/fanout-worker-0.0.1-SNAPSHOT.jar fanout-worker.jar

ENTRYPOINT ["java", "-jar", "fanout-worker.jar"]