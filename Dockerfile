FROM openjdk:11
COPY /build/libs/oyster-0.0.1-SNAPSHOT.jar /oyster.jar
CMD ["java","-jar", "oyster.jar"]
EXPOSE 8080