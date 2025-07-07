

# Step 1: Build the WAR file using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Step 2: Use Tomcat to serve the WAR
FROM tomcat:10.1.20-jdk17
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/Container123.war
