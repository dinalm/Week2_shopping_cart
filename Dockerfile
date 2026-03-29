FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

RUN apt-get update && apt-get install -y \
    openjfx \
    libgtk-3-0 \
    libxxf86vm1 \
    && rm -rf /var/lib/apt/lists/*

COPY target/ShoppingCartApp-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "--module-path", "/usr/share/openjfx/lib", \
            "--add-modules", "javafx.controls,javafx.fxml", \
            "-jar", "app.jar"]