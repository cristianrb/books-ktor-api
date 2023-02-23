FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=./books-ktor-api-all.jar
COPY ${JAR_FILE} /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]