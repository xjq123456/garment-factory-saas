FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/ai-selection-service-*.jar /app/application.jar
EXPOSE 8900
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/application.jar"]
