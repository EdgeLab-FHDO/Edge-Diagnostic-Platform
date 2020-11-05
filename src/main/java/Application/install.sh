apt-get update && apt-get install -y  ant git build-essential cmake curl openjdk-8-jdk libgtk2.0-dev pkg-config libv4l-dev libavcodec-dev libavformat-dev libswscale-dev python-dev python-numpy libtbb2 libtbb-dev libjpeg-dev libpng-dev libtiff-dev libjasper-dev libdc1394-22-dev

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

export ANT_HOME=/usr/bin/ant

mkdir opencv_bin && cd opencv_bin
mkdir modules

git clone https://github.com/opencv/opencv.git
git clone https://github.com/opencv/opencv_contrib.git

cp -R opencv_contrib/modules/aruco modules

cd opencv
mkdir build && cd build

cmake -DOPENCV_EXTRA_MODULES_PATH=/opencv_bin/modules /opencv_bin/opencv -D BUILD_SHARED_LIBS=OFF ..
make VERBOSE=1

mkdir /opencv-java-bin
mkdir /gen