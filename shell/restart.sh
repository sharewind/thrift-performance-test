source config.sh

#kill server
SERVER_PID=`ps auxf | grep "${MAIN_CLASS}" | grep -v "grep"| awk '{print $2}'`

if [ -n $SERVER_PID ] 
then
   echo "suc server pid is ${SERVER_PID}"
   kill $SERVER_PID
   echo "$SERVER_PID is killed!"
fi

cd ${DEPLOY_HOME}/classes
nohup java -cp .:../lib/* ${MAIN_CLASS} &
tail -f $LOG_FILE
