# Use an official Maven image as a parent image to build the application
FROM maven:3.8.4-openjdk-11 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code to the container
COPY src ./src

# Build the JAR file
RUN mvn package -DskipTests

# Create a separate runtime image
FROM openjdk:11-jre-slim

# Set the working directory in the runtime container
WORKDIR /app

# Copy the JAR file from the build container to the runtime container
# artifact name set here:  https://github.com/DawoudMahmud/FabFlix/blob/master/authentication_service/pom.xml#L20
COPY --from=build /app/target/authentication_service .

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "authentication_service.jar"]