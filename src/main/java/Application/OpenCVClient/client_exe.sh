while getopts i:m:b:s: flag
do
    case "${flag}" in
        i) clientid=${OPTARG};;
        m) masterurl=${OPTARG};;
        b) beatcommand=${OPTARG};;
        s) getservercommand=${OPTARG};;
    esac
done
export JAVA_HOME=/usr/local/jdk-11.0.2 && export PATH=$PATH:$JAVA_HOME/bin && java -Djava.library.path=gen/ -cp usr/share/java/jackson-core-2.4.2.jar:usr/share/java/jackson-databind-2.4.2.jar:usr/share/java/jackson-annotations-2.4.2.jar:usr/share/java/:gen/opencv-450.jar:. OpenCVClient CLIENT_ID=$clientid MASTER_URL=$masterurl BEAT_COMMAND=$beatcommand GET_SERVER_COMMAND=$getservercommand