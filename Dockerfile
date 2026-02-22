# Stage 1: Build all modules
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY notification-service/pom.xml ./notification-service/
COPY trade-execution-service/pom.xml ./trade-execution-service/
COPY signal-ingestion-service/pom.xml ./signal-ingestion-service/
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn package -DskipTests -B

# Stage 2: Notification Service
FROM eclipse-temurin:21-jre-alpine AS notification
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 3: Trade Execution Service
FROM eclipse-temurin:21-jre-alpine AS trade-execution
WORKDIR /app
COPY --from=build /app/trade-execution-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 4: Signal Ingestion Service
FROM eclipse-temurin:21-jre-alpine AS signal-ingestion
WORKDIR /app
COPY --from=build /app/signal-ingestion-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]