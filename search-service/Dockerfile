FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/search-service-*.jar /app/application.jar
EXPOSE 9000
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/application.jar"]
