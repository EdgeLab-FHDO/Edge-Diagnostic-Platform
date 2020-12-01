#move to /main to use
FROM ubuntu:16.04

#setup tools
COPY bash/OpenCVCommon/install.sh install.sh
COPY resources/OpenCV/opencv-450.jar /gen/opencv-450.jar
COPY resources/OpenCV/libopencv_java450.so /gen/libopencv_java450.so
RUN chmod +x install.sh && sync && ./install.sh && rm install.sh

#copy and build codes
COPY bash/OpenCVCommon/build_classes.sh build_classes.sh
COPY java/Application/Utilities/*.java /Application/Utilities/
COPY java/Application/MarkerGenerator/*.java /Application/MarkerGenerator/
WORKDIR /
RUN chmod +x build_classes.sh && sync && ./build_classes.sh && rm build_classes.sh

RUN export JAVA_HOME=/usr/local/jdk-11.0.2 && export PATH=$PATH:$JAVA_HOME/bin && java -Djava.library.path=gen/ -cp usr/share/java/jackson-core-2.4.2.jar:usr/share/java/jackson-databind-2.4.2.jar:usr/share/java/jackson-annotations-2.4.2.jar:usr/share/java/:gen/opencv-450.jar:. Application.MarkerGenerator.OpenCVClient.CreateMarker