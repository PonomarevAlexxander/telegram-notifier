FROM eclipse-temurin:21-jdk-alpine

COPY target/bot.jar app.jar

ENTRYPOINT ["sh", "-c", "java -jar /app.jar ${0} ${@}"]
