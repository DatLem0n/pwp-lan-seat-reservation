# Build stage
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app
COPY seat-reservation/.mvn/ .mvn/
COPY seat-reservation/mvnw seat-reservation/pom.xml ./
RUN ./mvnw dependency:go-offline
COPY seat-reservation/src ./src
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
