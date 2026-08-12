FROM gradle:8.10-jdk21 AS build

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean war --no-daemon


FROM payara/server-web:6.2024.6-jdk21

ENV JVM_ARGS="-Xms128m -Xmx320m -XX:MaxMetaspaceSize=176m -XX:+UseSerialGC -XX:TieredStopAtLevel=1"
ENV MEM_XSS=256k

COPY --from=build /app/build/libs/huellas-vet.war /opt/payara/deployments/huellas-vet.war
COPY --chown=payara:payara docker/init_2_reduce_threads.sh /opt/payara/scripts/init_2_reduce_threads.sh
RUN chmod +x /opt/payara/scripts/init_2_reduce_threads.sh