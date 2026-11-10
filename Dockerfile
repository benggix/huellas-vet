FROM gradle:8.10-jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew clean war --no-daemon

FROM tomcat:10.1-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

RUN sed -i 's|<Connector port="8080" protocol="HTTP/1.1"|<Connector port="8080" protocol="HTTP/1.1" maxThreads="20" minSpareThreads="2"|' /usr/local/tomcat/conf/server.xml \
 && grep -q 'maxThreads="20"' /usr/local/tomcat/conf/server.xml

COPY --from=build /app/build/libs/huellas-vet.war /usr/local/tomcat/webapps/ROOT.war

ENV JAVA_OPTS="-Xms32m -Xmx128m -XX:MaxMetaspaceSize=96m -XX:ReservedCodeCacheSize=32m -Xss256k -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -XX:-UsePerfData"

EXPOSE 8080