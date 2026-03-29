FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/ShoppingCartApp-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]