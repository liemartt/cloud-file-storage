FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test --no-daemon

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 8081
