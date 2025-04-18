#!/usr/bin/env bash

# bash는 return value가 안되니 *제일 마지막줄에 echo로 해서 결과 출력*후, 클라이언트에서 값을 사용한다

# 쉬고 있는 profile 찾기: real1이 사용중이면 real2가 쉬고 있고, 반대면 real1이 쉬고 있음
function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)
    echo "> [find_idle_profile] 응답 코드: $RESPONSE_CODE"

    if [ ${RESPONSE_CODE} -ge 400 ]
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    echo "> [find_idle_profile] 현재 profile: $CURRENT_PROFILE"

    if [ "$CURRENT_PROFILE" == "real1" ]; then
      IDLE_PROFILE=real2
    else
      IDLE_PROFILE=real1
    fi

    echo "> [find_idle_profile] 대기중 profile: $IDLE_PROFILE"
    echo "${IDLE_PROFILE}"
    return
}


# 쉬고 있는 profile의 port 찾기
function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == "real1" ]
    then
      echo "8081"
    else
      echo "8082"
    fi
    return
}