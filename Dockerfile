FROM gradle:8.10-jdk21 AS build

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean war --no-daemon


FROM payara/server-web:6.2024.6-jdk21

COPY --from=build /app/build/libs/huellas-vet.war /opt/payara/deployments/huellas-vet.war