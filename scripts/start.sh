#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=SmallWebServer

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
IDLE_PROFILE=$(find_idle_profile)

echo "> ${IDLE_PROFILE} Profile로 jar 파일 빌드를 실행합니다."

nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,classpath:/application-${IDLE_PROFILE}.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
  -Dspring.profiles.active=${IDLE_PROFILE} \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

cat $REPOSITORY/nohup.out
