source config.sh

cd ..
git pull origin dev
mvn -U clean package -P live -Dmaven.test.skip=true

if [ ! -f $DEPLOY_HOME ]
then
    mkdir -p $DEPLOY_HOME
fi

if [ -f "target/${PACKGET_NAME}" ]
then
       rm -rf ${DEPLOY_HOME}/*
       cp target/${PACKGET_NAME} ${DEPLOY_HOME}/
       cd ${DEPLOY_HOME}
       tar -zxvf ${PACKGET_NAME}
fi