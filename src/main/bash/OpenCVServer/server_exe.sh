#runs the OpenCVServer with parameters or from environment variables
while getopts i:a:m:r:b:c:p:v:n:e:o: flag
do
    case "${flag}" in
        i) serverid=${OPTARG};;
        a) serverip=${OPTARG};;
        m) masterurl=${OPTARG};;
        r) registerCommand=${OPTARG};;
        b) beatcommand=${OPTARG};;
        c) connected=${OPTARG};;
        p) port=${OPTARG};;
        v) interval=${OPTARG};;
        n) totalNetwork=${OPTARG};;
        e) totalResource=${OPTARG};;
        o) location=${OPTARG};;
    esac
done
export JAVA_HOME=/usr/local/jdk-11.0.2 && export PATH=$PATH:$JAVA_HOME/bin && java -Djava.library.path=gen/ -cp usr/share/java/jackson-core-2.4.2.jar:usr/share/java/jackson-databind-2.4.2.jar:usr/share/java/jackson-annotations-2.4.2.jar:usr/share/java/:gen/opencv-450.jar:. Application.MarkerDetection.OpenCVServer.OpenCVServer SERVER_ID=$serverid SERVER_IP=$serverip MASTER_URL=$masterurl REGISTER_COMMAND=$registerCommand BEAT_COMMAND=$beatcommand CONNECTED=$connected PORT=$port INTERVAL=$interval  TOTAL_NETWORK=$totalNetwork TOTAL_RESOURCE=$totalResource LOCATION=$location test