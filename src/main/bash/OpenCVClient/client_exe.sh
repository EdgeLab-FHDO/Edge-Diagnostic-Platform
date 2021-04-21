#runs the OpenCVClient with parameters or from environment variables
while getopts i:m:r:b:s:l:v:n:e:o:t:d:q: flag
do
    case "${flag}" in
        i) clientid=${OPTARG};;
        m) masterurl=${OPTARG};;
        r) registerCommand=${OPTARG};;
        b) beatcommand=${OPTARG};;
        s) getservercommand=${OPTARG};;
        l) latencyreportcommand=${OPTARG};;
        v) interval=${OPTARG};;
        n) reqNetwork=${OPTARG};;
        e) reqResource=${OPTARG};;
        o) location=${OPTARG};;
        t) latencyReportThreshold=${OPTARG};;
        d) disconnectCommand=${OPTARG};;
        q) maxEqr=${OPTARG};;
    esac
done
export JAVA_HOME=/usr/local/jdk-11.0.2 && export PATH=$PATH:$JAVA_HOME/bin && java -Djava.library.path=gen/ -cp usr/share/java/jackson-core-2.4.2.jar:usr/share/java/jackson-databind-2.4.2.jar:usr/share/java/jackson-annotations-2.4.2.jar:usr/share/java/:gen/opencv-450.jar:. Application.MarkerDetection.OpenCVClient.OpenCVClient CLIENT_ID=$clientid MASTER_URL=$masterurl REGISTER_COMMAND=$registerCommand BEAT_COMMAND=$beatcommand GET_SERVER_COMMAND=$getservercommand LATENCY_REPORT_COMMAND=$latencyreportcommand INTERVAL=$interval REQUIRED_NETWORK=$reqNetwork REQUIRED_RESOURCE=$reqResource LOCATION=$location LATENCY_REPORT_THRESHOLD=$latencyReportThreshold DISCONNECT_COMMAND=$disconnectCommand MAX_EQR=$maxEqr