apt-get update && apt-get install -y curl
curl -O https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_linux-x64_bin.tar.gz
tar zxvf openjdk-11.0.2_linux-x64_bin.tar.gz
mv jdk-11* /usr/local/
apt-get update && apt-get install -y  ant git build-essential cmake curl libgtk2.0-dev pkg-config libv4l-dev libavcodec-dev libavformat-dev libswscale-dev python-dev python-numpy libtbb2 libtbb-dev libjpeg-dev libpng-dev libtiff-dev libjasper-dev libdc1394-22-dev libjackson2-core-java libjackson2-databind-java libjackson2-annotations-java
export JAVA_HOME=/usr/local/jdk-11.0.2
export PATH=$PATH:$JAVA_HOME/bin

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