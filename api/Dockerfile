# Maven image
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

# Set working directory
WORKDIR /app

# Copy pom and wrapper first for dependency caching
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the full source
COPY src src

# Build the project and run tests
RUN ./mvnw clean install

# JDK 17 image
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy entire project including .mvn, wrapper, etc.
COPY --from=build /app /app

# Expose port
EXPOSE 8080

# Run the Spring Boot app
CMD ["./mvnw", "spring-boot:run"]
