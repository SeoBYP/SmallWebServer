#!/usr/bin/env bash

echo "> 현재 스크립트 절대경로: $0"
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
echo "> 현재 스크립트 디렉토리: $ABSDIR"
echo "> profile.sh 경로: ${ABSDIR}/profile.sh"
ls -al ${ABSDIR}/profile.sh

source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    echo "> 엔진엑스 Reload"
    sudo service nginx reload
}
