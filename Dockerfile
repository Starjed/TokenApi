FROM openjdk

WORKDIR /

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

EXPOSE 8080



