# 在模块目录执行: mvn package -DskipTests && docker build -t user-service:local .
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/user-service-*.jar /app/application.jar
EXPOSE 8100
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/application.jar"]
