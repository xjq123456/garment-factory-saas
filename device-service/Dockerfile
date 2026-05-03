FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/device-service-*.jar /app/application.jar
EXPOSE 8660
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/application.jar"]
