#!/usr/bin/env bash

# bash는 return value가 안되니 *제일 마지막줄에 echo로 해서 결과 출력* 후, 클라이언트에서 값을 사용한다

function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

    if [ ${RESPONSE_CODE} -ge 400 ]
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    >&2 echo "Current Profile : ${CURRENT_PROFILE}"

    if [ "${CURRENT_PROFILE}" == "real1" ]
    then
        IDLE_PROFILE=real2
    else
        IDLE_PROFILE=real1
    fi

    >&2 echo "Profile Name : ${IDLE_PROFILE}"
    echo "${IDLE_PROFILE}"  # 마지막 줄은 반드시 결과값만!
}


function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    >&2 echo ">>> find_idle_port 함수: $IDLE_PROFILE"
    if [[ "$IDLE_PROFILE" == "real1" ]]
    then
        echo "8081"
    else
        echo "8082"
    fi
}
