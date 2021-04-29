FROM adoptopenjdk/openjdk15:alpine-jre
EXPOSE 4444
RUN mkdir /DiagnosticsServer
COPY *.jar /DiagnosticsServer/server.jar
WORKDIR /DiagnosticsServer
ENTRYPOINT ["java", "-jar","./server.jar"]
#Use this to put arguments that pass as default to the app, otherwise pass them in docker run
CMD []