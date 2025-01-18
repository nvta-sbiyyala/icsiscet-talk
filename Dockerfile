# Use an official OpenJDK runtime with Java 21 (LTS)
FROM openjdk:23-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew .
COPY gradle/ gradle/

# Copy the project files to the container
COPY . .

# Grant execution permissions for the Gradle wrapper
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build

# Expose the port the application runs on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "build/libs/icsiscet-talk.jar"]
