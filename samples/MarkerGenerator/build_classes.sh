cp /opencv_bin/opencv/build/opencv-450.jar /gen/
cp /opencv_bin/opencv/lib/libopencv_java450.so /gen/

javac -cp gen/opencv-450.jar *.java