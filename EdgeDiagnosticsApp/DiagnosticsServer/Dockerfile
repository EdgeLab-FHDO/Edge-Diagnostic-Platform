#Docker file to create images from fatJar custom task in gradle multi-project build
#Use gradle multistage build, in the first stage install gradle and run the jar task
FROM gradle:6.6-jdk14-hotspot AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle fatJar

#Second stage, copy the generated jar into a new lightweight image with only JRE
FROM adoptopenjdk/openjdk14:jre-14.0.2_12-alpine
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/server.jar
ENTRYPOINT ["java", "-jar","/app/server.jar"]
#Use this to put arguments that pass as default to the app, otherwise pass them in docker build
CMD []