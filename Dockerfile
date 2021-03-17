#Docker file to create images from fatJar custom task in gradle multi-project build
#Use gradle multistage build, in the first stage install gradle and run the jar task
#FROM gradle:6.6-jdk14-hotspot AS build
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
#RUN gradle fatJar

#Second stage, copy the generated jar into a new lightweight image with only JRE
FROM adoptopenjdk/openjdk15:jre-15.0.2_7-alpine
EXPOSE 4567
RUN mkdir /EdgeDiagnostics
COPY *.jar /EdgeDiagnostics/platform.jar
WORKDIR /EdgeDiagnostics
ENTRYPOINT ["java", "-jar","./platform.jar"]
#Use this to put arguments that pass as default to the app, otherwise pass them in docker build
CMD []