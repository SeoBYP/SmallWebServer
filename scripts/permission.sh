#!/bin/bash
echo "> 실행 권한 부여 시작"
# 배포 대상 디렉토리에서만 chmod 수행
chmod +x /home/ec2-user/app/step3/zip/scripts/*.sh
echo "> 실행 권한 부여 완료"
