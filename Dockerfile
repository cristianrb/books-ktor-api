FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=./build/libs/books-ktor-api.jar
COPY ${JAR_FILE} /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]