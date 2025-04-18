#!/usr/bin/env bash

echo "> 현재 스크립트 절대경로: $0"
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
echo "> 현재 스크립트 디렉토리: $ABSDIR"
echo "> profile.sh 경로: ${ABSDIR}/profile.sh"
ls -al ${ABSDIR}/profile.sh

source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=SmallWebServer

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar 2>/dev/null | tail -n 1)

if [ -z "$JAR_NAME" ]; then
  echo "> ERROR: JAR 파일이 없습니다. 빌드가 제대로 되었는지 확인하세요."
  exit 1
fi

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

# 아래로 수정
chmod +x ${ABSDIR}/*.sh

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

if [ -z "$IDLE_PROFILE" ]; then
  echo "> ERROR: IDLE_PROFILE 값이 비어 있습니다. profile.sh 확인 필요."
  exit 1
fi

echo "> 실행할 profile: $IDLE_PROFILE"

nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,classpath:/application-${IDLE_PROFILE}.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
  -Dspring.profiles.active=${IDLE_PROFILE} \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
