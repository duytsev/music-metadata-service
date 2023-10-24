FROM openjdk:18.0.1
COPY build/libs/music-metadata-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
