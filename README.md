
<br>

### 📦 물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼 개발

<hr>

#### 개요

<img src="https://github.com/user-attachments/assets/6c9bd1db-8c11-42f6-badc-4b238b0ed5b4" alt="동갑내기 P들 drawio" width="80%">


> * 이번 프로젝트는 B2B 물류 관리 및 배송 시스템 입니다. B2B는 Business to Business의 약자로, 기업 간 거래를 의미합니다.
> * 스파르타 물류는 각 지역에 허브센터를 가지고 있으며 각 허브 센터는 여러 업체의 물건을 보관합니다.
> * 업체의 상품은 허브에서 필요한 경우 바로 허브로 전달됩니다.
> * 해당 상품의 배송 요청이 들어오면 목적지 허브로 물품을 이동시켜 목적지에 배송합니다.

<br><br>

## 👨‍👩‍👧‍👦 Our Team

![팀원소개](https://github.com/user-attachments/assets/7c5e2711-65e5-4ebb-b4c7-c5f8990c3348)
<br>

### [👊 프로젝트 노션 바로가기](https://teamsparta.notion.site/P-bb928c4debce4e348786372c08f081a7)

### 👊 GitHub
|조아영|한수빈|서병준|
|:---:|:---:|:---:|
|[@ayboori](https://github.com/ayboori)|[@subinny2](https://github.com/subinny2)|[@mad-cost](https://github.com/mad-cost)||
|presenter|security|leader|

<br><br>


## 프로젝트 기능

### 🛡 User

> * USER의 CRUD를 제공합니다.
> * User 탈퇴, 회원정보 수정, 나의 정보 조회, 회원 단일 및 목록 조회 api를 제공합니다.

### 📊 auth
 
> * JWT 발급 및 검증 API를 제공합니다.
> * 로그인, 로그아웃, 회원가입 API를 제공합니다.
> * 회원가입시 username, password는 정규표현식을 사용하여 유효성 검사를 확인합니다.

### ⛩️ Gateway
> * Auth 서비스의 검증 API를 호출하여, JWT검증을 수행합니다.
> * WebClient를 사용하여 Auth서비스와 통신하고, 인증이 완료된 요청만을 다음 서비스로 전달합니다.

### 🗨 Object

#### 🚚 Hub
> * 허브 이름, 주소, 위도, 경도를 포함하는 엔티티를 구현했습니다.
> * 변경이 잦지 않은 데이터이기 때문에, 캐싱하여 API 호출 빈도를 줄이고 빠르게 조회될 수 있게 했습니다.

#### 🏢 Company
> * 업체명, 업체 타입(생산업체, 수령업체), 업체 관리 허브 ID, 업체 주소를 포함하는 엔티티를 구현했습니다.

#### 🧳 Product
> * 상품명, 상품이 포함된 업체 ID, 상품 관리 허브 ID를 포함하는 엔티티를 구현했습니다.

### 🚚 Hub Route
> * 출발 허브 ID, 도착 허브 ID, 소요시간을 포함하는 엔티티를 구현했습니다.
> * 출발지와 도착지의 정보를 받으면, 최단 거리를 경로하는 허브 경로를 생성합니다.
> * 변경이 잦지 않은 데이터이기 때문에, 캐싱하여 API 호출 빈도를 줄이고 빠르게 조회될 수 있게 했습니다.

### 🚛 Delivery
> * 배송에 대한 현재 상태를 가지고 있으며, 배송 경로 기록에 의해서 바뀌게 됩니다.

### 🎁 Order
> * 주문이 생성, 삭제되면 배송 및 배송 경로 기록이 함께 생성, 삭제됩니다.
> * 주문, 배송, 배송 경로 기록이 같은 라이프 사이클을 가지고 있습니다.
> * feginClient를 사용하여 Auth Application에서 데이터를 검증합니다.

### 🗒️ DeliveryRecord
> * 배송 경로 기록은 최초에 모든 경로가 생성되어야 합니다.
> * 각각의 허브 이동 간의 상태가 변화합니다.

### 🚴 DeliveryPerson
> * 허브 이동 담당자와 업체 이동 담당자가 존재합니다.
> * 두 타입 간의 업무는 명확히 구분됩니다.
> * 허브 이동 담당자는 업체 배송을 할 수 없으며, 업체 배송 담당자는 허브 이동을 담당할 수 없습니다.

<hr>

### 🔍 검색 기능 (내용 검색, 페이징)

> * CRUD + Search (페이징) 기능을 구현했습니다.
> * 객체마다 토큰에 담긴 역할을 검사, 인가 후 권한이 있는 API에만 접근 가능하도록 했습니다.
> * is_delete 필드를 추가하여 논리적 삭제를 관리합니다.

<br><br>

## 적용 기술

### 🧾 Swagger
- 프론트엔드와 정확하고 원활한 소통을 위해 스웨거를 도입하여 적용했습니다.

### 🕵️ Ai
- Gemini API 연동하여 사용합니다.
- 해당 질문과 답변은 모두 저장 되어야 합니다.

### 🛡 Spring Security
- Gateway, JWT Token 등을 사용한 인증, 인가 기능을 구현했습니다.

### MSA 적용 및 서비스 간 통신
- FeignClient를 적용하여 각 서비스 간 데이터를 주고 받을 수 있도록 했습니다.
- WebClient를 적용하여 비동기 및 비차단 방식으로 HTTP 요청을 처리했습니다.


<br><br>



## ⚙ Development Environment

`Java Version 17` `SpringBoot Version 3.3.3` `Architecture MSA` `API Test Postman`


<br><br>

## 🚨 Trouble Shooting

#### CSRF token cannot be found 문제 [WIKI보기](https://github.com/B2B-Ai-Project/B2B-Ai-Project/wiki/%5BTrouble-Shooting%5D-An-expected-CSRF-token-cannot-be-found)

#### MSA의 Application 분리로 인한 배송 담당자를 어디서 생성해 줘야 하는가? [WIKI보기](https://github.com/B2B-Ai-Project/B2B-Ai-Project/wiki/%5BTrouble-Shooting%5D-MSA%EC%9D%98-Application-%EB%B6%84%EB%A6%AC%EB%A1%9C-%EC%9D%B8%ED%95%9C-%EB%B0%B0%EC%86%A1-%EB%8B%B4%EB%8B%B9%EC%9E%90%EB%A5%BC-%EC%96%B4%EB%94%94%EC%84%9C-%EC%83%9D%EC%84%B1%ED%95%B4-%EC%A4%98%EC%95%BC-%ED%95%98%EB%8A%94%EA%B0%80%3F)

#### 허브 이동 경로 구현 문제 (다익스트라 알고리즘) [WIKI보기](https://github.com/B2B-Ai-Project/B2B-Ai-Project/wiki/%5BTrouble-Shooting%5D-%ED%97%88%EB%B8%8C-%EA%B0%84-%EC%9D%B4%EB%8F%99-%EA%B2%BD%EB%A1%9C-%EA%B5%AC%ED%98%84-%EB%B0%A9%EC%8B%9D)

<br><br>

## 🌐 Infrastructure Design Document
<img src="https://github.com/user-attachments/assets/46a44525-d54b-41fa-bbf8-ae53146d79a7" alt="인프라 설계서" style="width:30`%; height:auto;">

<br>

## 📋 ERD Diagram
<img src="https://github.com/user-attachments/assets/c36c7fa4-b037-41ea-aae1-ea10b7652922" alt="ERD" style="width:50%; height:auto;">


<br>

## 📝 Technologies & Tools (BE)📝

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white"/> <br>
<img src="https://img.shields.io/badge/MSA-239120?style=for-the-badge&logo=Microservices&logoColor=white"/> <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=PostgreSQL&logoColor=white"/> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black"/> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/> <img src="https://img.shields.io/badge/Gemini-FFDD00?style=for-the-badge&logo=Gemini&logoColor=black"/>

<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"/> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <br/> <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>  <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"/> <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"/> <img src="https://img.shields.io/badge/Draw.io-FFB900?style=for-the-badge&logo=diagrams.net&logoColor=white"/>

<br><br><br><br>
