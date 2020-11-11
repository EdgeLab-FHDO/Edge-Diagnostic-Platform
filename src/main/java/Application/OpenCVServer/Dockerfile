#move to parent to use
FROM ubuntu:16.04

#setup tools
COPY install.sh install.sh
COPY Resources/opencv-450.jar /gen/opencv-450.jar
COPY Resources/libopencv_java450.so /gen/libopencv_java450.so
RUN chmod +x install.sh && sync && ./install.sh && rm install.sh

#copy and build codes
COPY build_classes.sh build_classes.sh
COPY OpenCVServer/*.java /Application/OpenCVServer/
COPY Utilities/*.java /Application/Utilities/
WORKDIR /
RUN chmod +x build_classes.sh && sync && ./build_classes.sh && rm build_classes.sh

#execute
COPY OpenCVServer/server_exe.sh server_exe.sh
WORKDIR /
RUN chmod +x server_exe.sh && sync && ./server_exe.sh && rm server_exe.sh