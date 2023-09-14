# Use an official Maven image as a parent image to build the application
FROM maven:3.8.4-openjdk-11 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and download dependencies
COPY pom.xml .

RUN mvn dependency:go-offline

# custom jar file built from https://github.com/GitCodings/Stack/compare/main...jdelnano:Stack:jdelnano/fixing-resource-fetching?expand=1
# logic added to `stack-core` was inspired by https://stackoverflow.com/questions/44399422/read-file-from-resources-folder-in-spring-boot
# as well as https://mkyong.com/java/java-getresourceasstream-in-static-method/
COPY ./stack-core-1.2.0.jar /root/.m2/repository/com/gitcodings/stack/stack-core/1.2.0/stack-core-1.2.0.jar

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
COPY --from=build /app/target/cs122b-be1-idm-service-starter-1.2.0.jar /app/target/authentication_service.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 8081

# Define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "./target/authentication_service.jar"]
CMD []
