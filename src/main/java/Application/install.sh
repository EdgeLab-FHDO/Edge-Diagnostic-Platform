apt-get update && apt-get install -y build-essential cmake curl libgtk2.0-dev pkg-config libv4l-dev libavcodec-dev libavformat-dev libswscale-dev python-dev python-numpy libtbb2 libtbb-dev libjpeg-dev libpng-dev libtiff-dev libjasper-dev libdc1394-22-dev libjackson2-core-java libjackson2-databind-java libjackson2-annotations-java
curl -O https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_linux-x64_bin.tar.gz
tar zxvf openjdk-11.0.2_linux-x64_bin.tar.gz
mv jdk-11* /usr/local/

export JAVA_HOME=/usr/local/jdk-11.0.2
export PATH=$PATH:$JAVA_HOME/bin