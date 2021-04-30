FROM adoptopenjdk/openjdk15:alpine-jre
RUN mkdir /DiagnosticsClient
COPY *.jar /DiagnosticsClient/client.jar
WORKDIR /DiagnosticsClient
ENTRYPOINT ["java", "-jar","./client.jar"]
#Use this to put arguments that pass as default to the app, otherwise pass them in docker run
CMD []