#move to /main to use
FROM ubuntu:16.04

#setup tools
COPY bash/OpenCVCommon/install.sh install.sh
COPY resources/OpenCV/opencv-450.jar /gen/opencv-450.jar
COPY resources/OpenCV/libopencv_java450.so /gen/libopencv_java450.so
COPY resources/OpenCVClient/singlemarkerssource.png /singlemarkerssource.png
RUN chmod +x install.sh && sync && ./install.sh && rm install.sh

#copy and build codes
COPY bash/OpenCVCommon/build_classes.sh build_classes.sh
COPY java/Application/MarkerDetection/OpenCVClient/*.java /Application/MarkerDetection/OpenCVClient/
COPY java/Application/Utilities/*.java /Application/Utilities/
WORKDIR /
RUN chmod +x build_classes.sh && sync && ./build_classes.sh && rm build_classes.sh

#execute
COPY bash/OpenCVClient/client_exe.sh client_exe.sh
WORKDIR /
RUN chmod +x client_exe.sh
#&& sync && ./client_exe.sh && rm client_exe.sh