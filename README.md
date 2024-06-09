# MyCarLong 프로젝트 README

## 1. 프로젝트 소개
MyCarLong은 Spring Boot 프레임워크와 React 라이브러리를 기반으로 구축된 웹 애플리케이션입니다.  
본 프로젝트는 사용자의 자동차 정보 관리, 자동차 관련 데이터 검색, 사용자 간 상호 작용 기능을 제공함으로써  
사용자의 자동차 경험을 향상시키는 것을 목표로 합니다.


## 2. 주요 기능
- **주변 시설 검색:**  사용자들은 현재 위치 반경 1km 이내의 주유소, 서비스 센터, 주차장, 세차장 등과 같은 자동차 관련 시설을 손쉽게 찾아볼 수 있습니다.
- **AI 챗봇:**  AI 챗봇 기능이 탑재되어 있습니다. 사용자들은 어떠한 자동차 관련 질문이든지 AI 챗봇에게 물어볼 수 있습니다.
- **자동차 커뮤니티:**  사용자들이 자동차 관련 경험과 정보를 서로 공유할 수 있는 공간을 제공합니다.
- **자동차 제원 조회:**  AI가 제공하는 정밀한 성능 정보를 통해, 다양한 차량의 상세 제원을 쉽게 확인할 수 있습니다.

## 3. Stack
- **Backend:** Spring Boot, Java, MySQL
- **Frontend:** React, TypeScript, JavaScript
- **Deploy:** AWS (EC2, S3) , Docker, Git Actions

![Stacks_Image](https://github.com/MyCarLong/MyCarLong/assets/118609415/c55ee09d-83d3-4957-a689-b2d9cf670e44)

<!--
---
## 4. Installation
### Requirements
- Spring Boot 3.x
- Java 17 or higher
- [Node.js](https://nodejs.org/en/download) 14 or higher
- JavaScript ES6

### Getting Started

```bash
$ git clone https://github.com/your-username/MyCarLong.git
$ cd MyCarLong
```

### Backend
```bash
$ cd backend
$ ./gradlew bootRun
```
### Frontend
```bash
$ cd frontend
$ npm start
```  
<br />-->
  
### 아키텍쳐
**프로젝트**
```dtd
MyCarLong 프로젝트 아키텍처 구조

├── backend/                # 백엔드 애플리케이션 모듈
│   ├── src/                # 소스 코드 디렉토리
│   │   ├── main/           # 메인 소스 코드
│   │   │   ├── java/       # Java 소스 코드
│   │   │   │   └── com/MyCarLong/
│   │   │   │       └── ...
│   │   │   └── resources/  # 리소스 파일 (설정 파일 등)
│   │   └── test/           # 테스트 소스 코드
│   └── build.gradle        # Gradle 빌드 스크립트
│
└── frontend/               # 프론트엔드 애플리케이션 모듈
    ├── public/             # 정적 파일 (HTML, 이미지 등)
    ├── src/                # 소스 코드 디렉토리
    │   ├── assets/         # 리소스 파일 (CSS, 이미지 등)
    │   ├── components/     # React 컴포넌트
    │   ├── pages/          # 페이지 컴포넌트
    │   ├── App.js          # 메인 애플리케이션 컴포넌트
    │   └── index.js        # 애플리케이션 진입점
    └── package.json        # npm 패키지 파일

```
**배포**
![Deploying](https://github.com/MyCarLong/MyCarLong/assets/118609415/5d71b871-1814-40b5-9ca6-5cb8d3f23a7b)


<br />

<!--
## 5. Document
- **API Documentation:** [https://your-domain/api-docs](https://your-domain/api-docs)
- **User Manual:** [https://your-domain/user-manual](https://your-domain/user-manual)  
  참고: 위 문서 링크는 예시이며, 실제 프로젝트 URL로 변경되어야 합니다.

[//]: # (## 6. License)

[//]: # (- **Spring Boot:** Apache License 2.0)

[//]: # (- **Java:** Oracle Binary Code License Agreement)

[//]: # (- **MySQL:** GNU General Public License)

[//]: # (- **React:** MIT License)

[//]: # (- **TypeScript:** Apache License 2.0)

[//]: # (- **Redux:** MIT License)

[//]: # (- **Git:** GNU General Public License)

[//]: # (  All rights reserved.)
-->
---
## 6. Team

### Frontend - [조광남](https://github.com/loganchodev)

- **Role:** React를 통한 사용자 인터페이스 및 프론트엔드 기능 구현
- **Technology Stack:** React, JavaScript, HTML5
<!--- **Interests:** 모던 웹 기술, UI/UX 디자인-->


### Team Leader/ Backend - [김근휘](https://github.com/leesoonshin)

- **Role:** 게시글 CRUD ,이미지크롤링 서비스 API 설계, AWS-Git Actions Docker를 통한 배포
- **Technology Stack:** Spring Boot, Java, MySQL
<!--- **Interests:** 대용량 데이터 처리, 데이터 보안, CI/CD-->

### Backend - [하동원](https://github.com/dd2558)

- **Role:** Oauth 2.0을 통한 소셜로그인 , Spring Security-JWT를 통한 로그인
- **Technology Stack:** Spring Boot, Java, MySQL
<!--- **Interests:** 클라우드 네이티브 기술, 마이크로서비스-->



