#runs the OpenCVServer with parameters or from environment variables
while getopts i:a:m:b:c:p: flag
do
    case "${flag}" in
        i) serverid=${OPTARG};;
        a) serverip=${OPTARG};;
        m) masterurl=${OPTARG};;
        b) beatcommand=${OPTARG};;
        c) connected=${OPTARG};;
        p) port=${OPTARG};;
    esac
done
export JAVA_HOME=/usr/local/jdk-11.0.2 && export PATH=$PATH:$JAVA_HOME/bin && java -Djava.library.path=gen/ -cp usr/share/java/jackson-core-2.4.2.jar:usr/share/java/jackson-databind-2.4.2.jar:usr/share/java/jackson-annotations-2.4.2.jar:usr/share/java/:gen/opencv-450.jar:. Application.MarkerDetection.OpenCVServer.OpenCVServer SERVER_ID=$serverid SERVER_IP=$serverip MASTER_URL=$masterurl BEAT_COMMAND=$beatcommand CONNECTED=$connected PORT=$port