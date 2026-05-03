FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/production-service-*.jar /app/application.jar
EXPOSE 8200
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/application.jar"]
