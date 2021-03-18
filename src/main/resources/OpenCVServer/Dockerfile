#move to parent to use
FROM ubuntu:16.04

#setup tools
COPY bash/OpenCVCommon/install.sh install.sh
COPY resources/OpenCV/opencv-450.jar /gen/opencv-450.jar
COPY resources/OpenCV/libopencv_java450.so /gen/libopencv_java450.so
RUN chmod +x install.sh && sync && ./install.sh && rm install.sh

#copy and build codes
COPY bash/OpenCVCommon/build_classes.sh build_classes.sh
COPY java/Application/Commons/*.java /Application/Commons/
COPY java/Application/MarkerDetection/OpenCVServer/*.java /Application/MarkerDetection/OpenCVServer/
COPY java/Application/Utilities/*.java /Application/Utilities/
WORKDIR /
RUN chmod +x build_classes.sh && sync && ./build_classes.sh && rm build_classes.sh

#execute
COPY bash/OpenCVServer/server_exe.sh server_exe.sh
WORKDIR /
RUN chmod +x server_exe.sh
#&& sync && ./server_exe.sh && rm server_exe.sh