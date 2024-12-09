FROM maven:3.9-amazoncorretto-21-alpine AS build
LABEL authors="kundu.vivasoft"

WORKDIR /app
COPY pom.xml /app

RUN mvn -f /app/pom.xml dependency:go-offline

COPY . /app

RUN mvn -f /app/pom.xml clean install -Dmaven.test.skip=true

FROM amazoncorretto:21-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar test-app.jar

EXPOSE 8093

CMD ["java", "-jar", "-server", "-Dspring.profiles.active=k8", "-XX:+UseStringDeduplication", "/app/test-app.jar"]