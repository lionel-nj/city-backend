FROM openjdk:11
COPY application/build/libs/city-backend*.jar /
WORKDIR /
EXPOSE 8080
ENTRYPOINT [ "/bin/sh", "-c", "java -jar city-backend*.jar"]
