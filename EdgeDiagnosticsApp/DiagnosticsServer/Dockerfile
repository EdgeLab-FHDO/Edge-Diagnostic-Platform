FROM adoptopenjdk/openjdk15:alpine-jre
RUN mkdir /DiagnosticsServer
COPY *.jar /DiagnosticsServer/client.jar
WORKDIR /DiagnosticsServer
ENTRYPOINT ["java", "-jar","./server.jar"]
#Use this to put arguments that pass as default to the app, otherwise pass them in docker run
CMD []