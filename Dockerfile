FROM openjdk:11
COPY application/build/libs/application-*-all.jar /
WORKDIR /
EXPOSE 8080
ENTRYPOINT [ "/bin/sh", "-c", "java -jar application-*-all.jar"]
