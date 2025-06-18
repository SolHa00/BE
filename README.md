# OurHood
> ### 우리만의 공간, 특별한 추억


## 📸 새로운 사진 공유 플랫폼 OurHood를 소개해요!
- **OurHood는 가족, 친구, 팀, 동아리 등 개인적인 조직 단위로 추억과 사진을 공유할 수 있는 플랫폼입니다.**
- **우리만의 방에서만 소통하며, 안전하고 따뜻한 공간을 경험해보세요!**


## 🔥OurHood 핵심 기능

[//]: # (업데이트 예정)

## System Architecture

[//]: # (업데이트 예정)


## 📁 Application Architecture
- Domain-Driven Design (DDD) 기반으로 설계되었습니다.

```bash
📁server/
   └── ourhood/
       ├── domain/ # 주요 도메인
       │   ├── auth/ # 인증 관련 기능 모듈 (OAuth, JWT 등)
       │   ├── comment/ # 댓글 관리
       │   ├── common/ # 공통 엔티티
       │   ├── invitation/ # 초대 요청 관리
       │   ├── join/ # 참여 요청 관리
       │   ├── moment/ # 모먼트(사진) 관리
       │   ├── room/ # 방 관리
       │   └── user/ # 사용자 관리
       └──  global/ # 애플리케이션 전반에 걸쳐 사용되는 공통 기능
            ├── auth/ # 인증 필터 및 어노테이션
            ├── config/ # 애플리케이션 설정 (JPA, Redis, Security, Web)
            ├── exception/ # 예외 처리
            ├── redis/ # Redis 관련 서비스
            └── response/ # 공통 응답 형식
```
<br>



## 🛠 Tech Stack

| 구분             | 기술 스택                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
|----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Framework      | <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-social&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-social&logo=Gradle&logoColor=white">                                                                                                                                                                                                                                                                                                                         |
| ORM            | <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-social&logo=Databricks&logoColor=white">                                                                                                                                                                                                                                                                                                                                                                                                                              |
| Authorization  | <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-social&logo=springsecurity&logoColor=white">  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-social&logo=jsonwebtokens&logoColor=white" />                                                                                                                                                                                                                                                                                                           |
| Database       | <img src="https://img.shields.io/badge/MySQL-4479A1.svg?style=for-the-social&logo=MySQL&logoColor=white">   <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-social&logo=redis&logoColor=white" />                                                                                                                                                                                                                                                                                                                               |
| CI/CD & DevOps | <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-social&logo=githubactions&logoColor=white"> <img src ="https://img.shields.io/badge/AWS CodeDeploy-6DB33F?style=for-the-social&logo=awscodedeploy&logoColor=white">                                                                                                                                                                                                                                                                                                    |
| Infrastructure | <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-social&logo=nginx&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-social&logo=docker&logoColor=white"> <img src ="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-social&logo=amazonec2&logoColor=white">  <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-social&logo=amazonrds&logoColor=white"> <img src ="https://img.shields.io/badge/AWS S3-69A31?style=for-the-social&logo=amazons3&logoColor=white"> |

## 📈 DataBase Schema
### MySQL Schema

<img src="https://ourhood-s3-bucket.s3.ap-northeast-2.amazonaws.com/assets/ourhood-erd.png" width="500" height="314">

## 👥 Contributors

| FE                                                                                                                    |
|-----------------------------------------------------------------------------------------------------------------------|
| <a href="https://github.com/dongha-choi"><img src="https://github.com/dongha-choi.png" alt="profile" width="140"></a> | 
| [최동하](https://github.com/dongha-choi)                                                                                 |


| BE                                                                                                             |
|----------------------------------------------------------------------------------------------------------------|
| <a href="https://github.com/so1eeee"><img src="https://github.com/so1eeee.png" alt="profile" width="140"> </a> |
| [정 솔](https://github.com/so1eeee)                                                                              | 