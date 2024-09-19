
<br>

### 물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼 개발

<hr>

#### 개요

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
|BE|BE|BE|

<br><br>


## 프로젝트 기능

### 🛡 login

> * Kakao와 Naver계정을 통한 간편 로그인이 가능합니다.
> * Kakao Email과 Naver Email 이 동일한 경우 하나의 계정으로 통합하여 사용이 가능합니다.
> * 하나의 계정에 Kakao 와 Naver 모두 연동되어있는 경우 두 곳 모두에서 연동이 해제되며 서비스에서 탈퇴처리 됩니다.
> * 작성한 게시글, 댓글 등은 사라지지 않으며 탈퇴한계정으로 표시됩니다(회원 정보는 삭제됨).


### 📊 auth
 
> * 스케쥴러를 활용하여 5분마다 데이터를 수집합니다.
> * 2-1. 구 별 코로나 정보, spot 별 날씨 정보 제공 : 저장되어 있는 데이터를 실시간으로 제공합니다. 
> * 2-2. 전체 데이터의 누적 통계를 활용한 정보제공 기능 : 수집한 데이터를 기반으로 혼잡도 점수를 산정 하여 순위 통계를 제공합니다.
> * 2-3. spot 별 누적 + 실시간 정보제공 기능 : 지난주 같은 요일의 혼잡도, 인구수를 비교하여 실시간 인구 추이를 제공합니다.

### 🗨 hub
 
> * 서울시 25개 구 별 커뮤니티를 제공합니다.
> * 1.게시글, 댓글 작성/수정/삭제/조회 : 다중 이미지 업로드가 가능하며 카테고리 선택과 태그추가 기능을 지원합니다.
> * 2.좋아요 : 게시글, 댓글을 좋아요 할 수 있으며 이에대한 알림기능도 지원합니다.
> * 3.북마크 : 내가 자주 사용하는 구를 북마크 할 수 있습니다. 북마크한 지역의 게시글이 추가되면 실시간 알림을 제공합니다.

### 🔍 검색 기능(내용+태그, 태그 검색기능)

> * QueryDSL을 활용하여 동적 쿼리작성이 가능하도록 구현하였습니다.
> * 게시글의 내용을 검색하거나 태그로 검색이 가능합니다. 작성자 검색도 가능합니다.

### 👨‍💻 마이페이지 기능 (내가 작성한 글, 내가 좋아요한 글, 내 게시글에 달린 댓글, 내 정보 수정)
 
> * 마이페이지에서 내가 작성한글, 내가 좋아요한 글, 내 게시글에 달린 댓글을 확인할 수 있으며 프로필사진과 닉네임을 수정할 수 있습니다.
> * 내 게시글에 새로운 댓글이 달리면 새로운 알림이 등록됩니다.

### 📢 신고 기능 (사용자, 닉네임, 게시글, 댓글)
 
> * 악성 사용자, 불건전한 닉네임, 게시글, 댓글을 내용과 함께 신고할 수 있습니다.
> * 본인은 본인을 신고할 수 없으며 같은 건의 신고에 대해서는 계정 하나당 1회로 제한됩니다.
> * 항목별 일정 횟수가 지나게 되면 강제로 닉네임을 변경하거나, 내용을 비공개 처리하여 보여주게 됩니다.

### 💬 WebSocket을 활용한 실시간 채팅

> * 실시간 채팅이 가능합니다.
> * 최근에 대화가 이루어진 순서대로 채팅방이 보여집니다.
> * 상대방이 나간 후에 새로운 메세지가 등록되면 기존의 채팅이 이어집니다.
> * 채팅방에서 모두 나가게 되면 채팅 내역은 삭제되며 다시 대화를 시작하면 새로운 채팅방이 생성됩니다.

### 🔔 SSE를 활용한 실시간 알림

> * 북마크한 게시판에 새로운 글이 등록되면 실시간 알림을 제공하며 알림을 클릭하면 해당 게시글로 이동합니다.
> * 내가 작성한 게시글에 좋아요 및 댓글이 달리면 실시간 알림을 제공하며 알림을 클릭하면 해당 게시글로 이동합니다.
> * 실시간 채팅이 오면 실시간 알림을 제공하며 알림을 클릭하면 해당 채팅방으로 이동합니다.
<br><br>

## 적용 기술

### ◻ QueryDSL

> 정렬, 검색어 등에 따른 동적 쿼리 작성을 위하여 QueryDSL 도입하여 활용했습니다.


### ◻ Swagger

> 프론트엔드와 정확하고 원활한 소통을 위하여 스웨거를 도입하여 적용하였습니다.         
> [BoombiBoombi Swagger](https://boombiboombi.o-r.kr/swagger-ui/index.html#/)


### ◻ Sentry를 통한 에러 로그 확인 및 공유

> Sentry를 활용하여 에러로그를 쉽게 확인/공유 할 수 있었습니다.         
> [Sentry Image](https://github.com/HH9C4/BBBB-BE/wiki/%5BTech%5D-Sentry)


### ◻ Github Actions & Code Deploy (CI/CD)

> 자동 빌드/배포를 위하여 깃허브 액션과 코드디플로이를 활용하여 CI/CD 를 구축했습니다.         
> [AWS CodeDeploy](https://github.com/HH9C4/BBBB-BE/wiki/%5BTech%5D-AWS-CodeDeploy)

### ◻ Nginx (무중단배포)

> 서비스 운영중 업데이트를 위한 재배포시 중단없는 서비스 제공을 위하여 Nginx 와 Shell Script를 활용해 무중단배포를 구현하였습니다.       


### ◻ Scheduler를 통한 open api 호출
 
> 5분마다 변동되는 데이터를 수집/제공/관리 하기 위하여 스케쥴러를 활용하였습니다.


### ◻ Redis

> 연속된 요청으로 인한 DB병목을 해소하고 RefreshToken 등 소멸기간이 존재하는 데이터의 TimeToLive 관리를 용이하게 할 수 있도록 Redis를 도입하였습니다.

<br><br>



## ⚙ Development Environment

`Java Version 17` `SpringBoot Version 3.3.3` `Architecture MSA` `API Test Postman`


<br><br>

## 🚨 Trouble Shooting

#### Join Fetch 순서 보장 문제 [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-Join-Fetch-%EC%88%9C%EC%84%9C-%EB%B3%B4%EC%9E%A5-%EB%AC%B8%EC%A0%9C)

#### NGINX ReverseProxy를 활용한 무중단배포 시 Cors, SSE 설정 [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-NGINX-ReverseProxy%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%AC%B4%EC%A4%91%EB%8B%A8%EB%B0%B0%ED%8F%AC-%EC%8B%9C-CORS,-SSL%EC%9D%B8%EC%A6%9D%EC%84%9C-,-SSE-%EC%84%A4%EC%A0%95)

#### JPA N+1 문제 [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-JPA-N%E2%9E%951-%EB%AC%B8%EC%A0%9C)

#### Open Api data [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-Open-Api-Data-Handling)

#### Token Reissue [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-Token-Reissue)

#### Redis 크롤러 봇 및 인증설정 [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-Redis-%ED%81%AC%EB%A1%A4%EB%9F%AC-%EB%B4%87-%EB%B0%8F-%EC%9D%B8%EC%A6%9D%EC%84%A4%EC%A0%95)


<br><br>

## :raising_hand::thought_balloon: Concern

#### Access Token and Refresh Token Reissue Process [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BConcern%5D-Access-Token-and-Refresh-Token-Reissue-Process)

#### Comment & Like Table Structure [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BConcern%5D-Comment-&-Like-Table-Structure)

#### S3 Image Upload Control [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BConcern%5D-S3-Image-Upload-Control)

#### Validation Logic [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BConcern%5D-Validation-Logic)

<br><br>

## 🌐 Infrastructure Design Document
<img src="https://github.com/user-attachments/assets/46a44525-d54b-41fa-bbf8-ae53146d79a7" alt="인프라 설계서" style="width:30`%; height:auto;">

<br>

## 📋 ERD Diagram
<img src="https://github.com/user-attachments/assets/c36c7fa4-b037-41ea-aae1-ea10b7652922" alt="ERD" style="width:50%; height:auto;">


<br>

## 📝 Technologies & Tools (BE) 📝

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white"/> <br>
<img src="https://img.shields.io/badge/MSA-239120?style=for-the-badge&logo=Microservices&logoColor=white"/> <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=PostgreSQL&logoColor=white"/> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black"/> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/>

<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"/> <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>  <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"/> <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"/> <img src="https://img.shields.io/badge/Draw.io-FFB900?style=for-the-badge&logo=diagrams.net&logoColor=white"/>

<br><br><br><br>
