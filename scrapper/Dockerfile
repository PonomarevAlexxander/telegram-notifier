FROM eclipse-temurin:21-jdk-alpine

COPY target/scrapper.jar app.jar

ENTRYPOINT ["sh", "-c", "java -jar /app.jar ${0} ${@}"]
