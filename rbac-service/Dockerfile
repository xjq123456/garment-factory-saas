# 在模块目录执行: mvn package -DskipTests && docker build -t rbac-service:local .
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/rbac-service-*.jar /app/application.jar
EXPOSE 8110
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/application.jar"]
