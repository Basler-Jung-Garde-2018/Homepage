FROM maven:3.8.7-openjdk-21 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /workspace/target/quarkus-app/lib/ /app/lib/
COPY --from=build /workspace/target/quarkus-app/*.jar /app/
COPY --from=build /workspace/target/quarkus-app/app/ /app/app/
COPY --from=build /workspace/target/quarkus-app/quarkus-run.jar /app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/quarkus-run.jar"]
