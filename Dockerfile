FROM eclipse-temurin:21
COPY target/*.jar app.jar
ENTRYPOINT java $JAVA_OPTIONS -jar app.jar