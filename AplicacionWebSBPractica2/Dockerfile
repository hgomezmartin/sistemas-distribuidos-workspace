# spring-app/Dockerfile
FROM eclipse-temurin:22-jre-alpine
WORKDIR /app

#nombre del jar y lo copiamos dentro del contenerdor app.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

#exponemos en el puerto 8080 + comando de arranque
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
