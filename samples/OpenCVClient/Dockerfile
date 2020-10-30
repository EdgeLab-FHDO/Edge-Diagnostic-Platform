FROM ubuntu:16.04

COPY install.sh install.sh
COPY build_classes.sh build_classes.sh
COPY ImageProcessor.java ImageProcessor.java
COPY OpenCVUtil.java OpenCVUtil.java
COPY DetectMarker.java DetectMarker.java
COPY OpenCVClient.java OpenCVClient.java
RUN chmod +x install.sh && sync && ./install.sh && rm install.sh
WORKDIR /
RUN chmod +x build_classes.sh && sync && ./build_classes.sh && rm build_classes.sh

RUN bash
#CMD java -Djava.library.path=gen/ -cp gen/opencv-450.jar:. OpenCVClient