FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle build.gradle gradle.properties ./
COPY src src

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/build/libs/truck-booking-platform.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
