# =========================================================================
# STAGE 1: Build & Compilation (Heavyweight SDK Image)
# =========================================================================
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /build

# Copy Maven wrapper configuration and dependencies first (Leverages Docker caching layers)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy source code and build the application (Skips testing for speed; Jenkins will handle verification)
COPY src ./src
RUN ./mvnw clean package -DskipTests

# =========================================================================
# STAGE 2: Secure Runtime Environment (Minimal JRE Image)
# =========================================================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-root system user and group for DevSecOps hardening
RUN groupadd -g 10001 appgroup && \
    useradd -u 10001 -g appgroup -m -s /bin/bash appuser

# Copy the compiled JAR artifact from the builder stage
COPY --from=builder /build/target/book-store-app-0.0.1-SNAPSHOT.jar app.jar

# Adjust ownership of the application file to our non-root user
RUN chown appuser:appgroup app.jar

# Switch execution context to the secure non-root user
USER appuser

# Expose the default Spring Boot web port
EXPOSE 8080

# Configure JVM flags for optimized container container awareness and memory management
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:+ExitOnOutOfMemoryError", "-jar", "app.jar"]
