# Spring Boot + AWS CRUD Project

간단한 게시글 작성 기능을 중심으로, **Spring Boot**와 **AWS** 환경을 활용한 OAuth2 인증 + CRUD 프로젝트입니다.  
Google & Naver 로그인, 게시글 작성 API, **GitHub Actions + CodeDeploy를 이용한 EC2 자동 배포**까지 포함되어 있습니다.

---

## 프로젝트 개요

### ⏱ 개발 기간
**2025.03 ~ 2025.04 (약 1개월)**

### 🛠️ 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: MariaDB (AWS RDS)
- **ORM**: JPA (Hibernate)
- **Auth**: OAuth2 (Google, Naver)
- **Testing**: JUnit
- **CI/CD**: GitHub Actions + AWS CodeDeploy
- **Infra**: AWS EC2, RDS, S3

---

## ✅ 주요 기능

### 🔐 OAuth2 로그인
- **Google 로그인 연동** (Spring Security 기반)
- **Naver 로그인 연동** (Custom OAuth2UserService 구현)
- 외부 도메인에서도 리디렉션이 잘 작동하도록 설정:
    - `application-oauth.properties`에 다음 추가:
      ```properties
      server.forward-headers-strategy=framework
      ```
    - Spring Boot가 `X-Forwarded-*` 헤더를 기반으로 base URL을 계산해 **EC2 퍼블릭 주소 자동 대응 가능**

### 📝 게시글 API
- 로그인 사용자만 **글 등록 가능**
- 작성자 정보는 자동으로 저장
- 추후 조회, 수정, 삭제 API 확장 예정

### 🔄 자동 배포
- GitHub에 Push하면 자동으로 JAR 빌드 → S3 업로드 → EC2 배포
- AWS CodeDeploy가 `appspec.yml` + `deploy.sh` 기반으로 서버 자동 재실행
- **무중단 배포 구현 완료**:
    - `nginx` 리버스 프록시 + Health Check 기반 이중 포트 배포 구조
    - 이전에는 `find_idle_profile` 로그 출력 오류와 `nginx` 설정 파일 충돌로 인해 502 오류 발생함
    - `/etc/nginx/conf.d/springboot.conf`가 8080 고정이라 충돌 → `.bak` 처리 후 해결

### 🧪 테스트
- JUnit 기반 단위 테스트 작성 (PostService 등)

---

## ☁️ 배포 환경

### EC2 자동 배포
- GitHub Actions + AWS CodeDeploy 연동
- `.github/workflows/deploy.yml`에 정의된 Workflow로 Push 시 자동 실행
- S3에 zip 파일 업로드 → CodeDeploy가 EC2로 전송 및 실행

### RDS 연동
`application.yml` 설정 예시:
```yaml
spring:
  datasource:
    url: jdbc:mariadb://[RDS-ENDPOINT]:3306/[DB명]
    username: [username]
    password: [password]
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MariaDBDialect
```

## 📚 개발 목표 & 회고

- Spring Security + OAuth2 인증 흐름에 대한 이해
- JPA 기반 Entity 설계 및 CRUD 구현
- AWS 인프라 사용 경험 (배포 및 DB 연동)
- GitHub Actions와 CodeDeploy를 활용한 CI/CD 구축
- 테스트 코드 작성으로 안정성 확보
- **무중단 배포 구조의 실제 적용 및 Nginx 문제 해결 경험 축적**
