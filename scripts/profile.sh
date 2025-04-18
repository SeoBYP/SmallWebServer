#!/usr/bin/env bash

# bash는 return value가 안되니 *제일 마지막줄에 echo로 해서 결과 출력*후, 클라이언트에서 값을 사용한다

function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)
    echo "> [find_idle_profile] 응답 코드: $RESPONSE_CODE"

    if [ "${RESPONSE_CODE}" -ge 400 ]; then
        CURRENT_PROFILE="real2"
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile | tr -d '\r')
    fi

    echo "> [find_idle_profile] 현재 profile: $CURRENT_PROFILE"

    if [ "$CURRENT_PROFILE" = "real1" ]; then
        IDLE_PROFILE="real2"
    elif [ "$CURRENT_PROFILE" = "real2" ]; then
        IDLE_PROFILE="real1"
    else
        echo "> [find_idle_profile] 알 수 없는 profile: '$CURRENT_PROFILE'. 기본값 'real2' 사용"
        IDLE_PROFILE="real2"
    fi

    echo "> [find_idle_profile] 대기중 profile: $IDLE_PROFILE"
    echo "${IDLE_PROFILE}"
}

function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ "$IDLE_PROFILE" = "real1" ]; then
        echo "8081"
    else
        echo "8082"
    fi
}
