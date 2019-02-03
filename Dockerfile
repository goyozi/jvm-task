FROM openjdk:11

COPY build/libs/*.jar app.jar

EXPOSE 8080

CMD java -jar app.jar