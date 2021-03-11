#runs the OpenCVClient with parameters or from environment variables
while getopts i:m:b:s:r: flag
do
    case "${flag}" in
        i) clientid=${OPTARG};;
        m) masterurl=${OPTARG};;
        b) beatcommand=${OPTARG};;
        s) getservercommand=${OPTARG};;
        r) latencyreportcommand=${OPTARG};;
    esac
done
export JAVA_HOME=/usr/local/jdk-11.0.2 && export PATH=$PATH:$JAVA_HOME/bin && java -Djava.library.path=gen/ -cp usr/share/java/jackson-core-2.4.2.jar:usr/share/java/jackson-databind-2.4.2.jar:usr/share/java/jackson-annotations-2.4.2.jar:usr/share/java/:gen/opencv-450.jar:. Application.MarkerDetection.OpenCVClient.OpenCVClient CLIENT_ID=$clientid MASTER_URL=$masterurl BEAT_COMMAND=$beatcommand GET_SERVER_COMMAND=$getservercommand LATENCY_REPORT_COMMAND=$latencyreportcommand