# Fase di build
FROM ubuntu:latest AS build

# Installare le dipendenze
RUN apt-get update && apt-get install -y openjdk-17-jdk

# Imposta la directory di lavoro
WORKDIR /app

# Copia i file di progetto
COPY . .

# Assicurati che gradlew abbia i permessi di esecuzione
RUN chmod +x gradlew

# Costruisci il file JAR dell'applicazione
RUN ./gradlew bootJar --no-daemon

# Fase di runtime
FROM openjdk:17-jdk-slim

# Espone la porta 8080
EXPOSE 8080

# Copia il file JAR dalla fase di build
COPY --from=build /app/build/libs/Maxele-1.jar app.jar
COPY --from=build /app/graph.json graph.json

# Imposta il comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]
