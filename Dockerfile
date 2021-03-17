#Docker file to create images from fatJar custom task
FROM adoptopenjdk/openjdk15:jre-15.0.2_7-alpine
EXPOSE 4567
RUN mkdir /EdgeDiagnostics
#copy the generated jar into a lightweight image with only JRE
COPY *.jar /EdgeDiagnostics/platform.jar
WORKDIR /EdgeDiagnostics
ENTRYPOINT ["java", "-jar","./platform.jar"]
#Use this to put arguments that pass as default to the app, otherwise pass them in docker build
CMD []