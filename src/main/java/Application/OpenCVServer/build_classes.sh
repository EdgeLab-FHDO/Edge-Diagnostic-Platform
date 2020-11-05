cp /opencv_bin/opencv/build/opencv-450.jar /gen/
cp /opencv_bin/opencv/lib/libopencv_java450.so /gen/

javac -cp gen/opencv-450.jar:usr/share/java/jackson-core-2.4.2.jar:usr/share/java/jackson-databind-2.4.2.jar:usr/share/java/jackson-annotations-2.4.2.jar *.java