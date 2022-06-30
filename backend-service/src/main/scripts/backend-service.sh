PROJECT_PATH="${M2_REPO}/episen/backend-service/1.0-SNAPSHOT"
JAR_FILE="backend-service-1.0-SNAPSHOT-jar-with-dependencies.jar"
CLASS_PATH=${PROJECT_PATH}/${JAR_FILE}
MAIN_CLASS="episen.backend.server.BackendService"
#exec java -cp ${CLASS_PATH} ${MAIN_CLASS} $*
echo ${M2_HOME}
classpth="${M2_HOME}\\episen\\backend-service\\1.0-SNAPSHOT\\backend-service-1.0-SNAPSHOT-jar-with-dependencies.jar"
exec java -cp /home/tata/Alex/backend-service-1.0-SNAPSHOT-jar-with-dependencies.jar episen.backend.server.BackendService $*