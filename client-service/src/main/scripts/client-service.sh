PROJECT_PATH="${M2_REPO}/episen/client-service/1.0-SNAPSHOT"
JAR_FILE="client-service-1.0-SNAPSHOT-jar-with-dependencies.jar"
CLASS_PATH=${PROJECT_PATH}/${JAR_FILE}
MAIN_CLASS="client.Client"
java -cp ${CLASS_PATH} ${MAIN_CLASS} $*