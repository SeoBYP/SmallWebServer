# Spring Boot + AWS CRUD Project

간단한 게시글 작성 기능을 중심으로, **Spring Boot**와 **AWS** 환경을 활용한 OAuth2 인증 + CRUD 프로젝트입니다.  
Google & Naver 로그인, 게시글 작성 API, AWS 배포 경험까지 포함되어 있습니다.

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
- **Infra**: AWS EC2, RDS

---

## ✅ 주요 기능

### 🔐 OAuth2 로그인
- **Google 로그인 연동** (Spring Security 기반)
- **Naver 로그인 연동** (Custom OAuth2UserService 구현)

### 📝 게시글 API
- 로그인 사용자만 **글 등록 가능**
- 작성자 정보는 자동으로 저장
- 추후 조회, 수정, 삭제 API 확장 예정

### 🧪 테스트
- JUnit 기반 단위 테스트 작성 (PostService 등)

---

## ☁️ 배포 환경

### EC2 배포
- Ubuntu EC2 인스턴스에서 직접 Git Clone → Build → 실행
```bash
./gradlew build  
java -jar build/libs/*.jar
```

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

---

## 📸 예시 화면
- [ ] Google 로그인 성공 후 게시글 작성
- [ ] RDS에 저장된 글 확인
- [ ] Naver 로그인 콜백 처리

(이미지는 필요 시 추가!)

---

## 📚 개발 목표 & 회고

- Spring Security + OAuth2 인증 흐름에 대한 이해
- JPA 기반 Entity 설계 및 CRUD 구현
- AWS 인프라 사용 경험 (배포 및 DB 연동)
- 테스트 코드 작성으로 안정성 확보


