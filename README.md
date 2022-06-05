# Capstone-Server
2022-1 Ajou SW Capstone Project 아주만능이조  
Server Repository  

## 프로젝트 소개  
### 삼삼오오: 최적의 나눔 위치를 제안해주는 배달 공동구매 플랫폼
사용자 위치 기반의 배달 공동구매 전문 플랫폼  
1) 배달 공동구매로 인한 경제적 부담 감소  
2) 편리하고 안전한 공동구매 과정 제공  

## 기술 스택  
![기술스택](https://user-images.githubusercontent.com/69350945/172059611-6b25ccb7-df13-41c7-934d-67d5116843bd.png)
### 주요 기술
* React  
: Front-end 개발  

* Spring Boot  
: Back-end 개발  

* Flask  
: 인공지능 모델 학습, 주문 자동 접수 서버  

* Amazon EC2  
: 웹서버 구축  

* Amazon RDS  
: AWS 클라우드 데이터베이스 관리 시스템  

* MySQL  
: 서버용 데이터베이스(RDBMS)  

* Firebase  
: 채팅 구현용 데이터베이스(NoSQL)  

* Docker  
: 서버 배포용  

### 외부 API
* 카카오맵 API  
: 위치 정보 출력, 나눔 위치 계산 시 대표장소 탐색  

* TMAP API  
: 나눔 위치 계산 시 도보 거리 계산  

* I'mport API    
: 포인트 충전 및 결제 시스템  

* CoolSMS API  
: 휴대폰 인증  

## 주 기능
### 나눔 게시글 조회/등록/참여
![나눔 게시글 조회](https://user-images.githubusercontent.com/69350945/172060675-65911757-13c0-474f-9093-cb2133e6ad17.png)

    나눔 참여 형식 정형화  
    -> 편리한 배달 공동구매 멤버 모집  

### "여기서 모여"
![여기서-모여](https://user-images.githubusercontent.com/69350945/172060913-86eb2be7-d73e-4e69-a05c-2dc4621af8c8.png)

    서비스가 알고리즘을 통해 나눔 멤버 간 최적의 나눔위치를 계산해 제안  
    -> 나눔 위치 설정을 위한 과정 생략  

### AI를 통한 모집완료 시간 예측
    인공지능 모델을 통해 식당/주문예정시간/요일에 따른 모집 완료 시간 예측  
    -> 모집완료 시간 예측 정보를 통해 식당 결정에 도움  

### 식당 정보 자동 완성
![식당정보-자동완성](https://user-images.githubusercontent.com/69350945/172060916-91adabb9-fa64-4404-80fd-b4e17e9f3706.png)

    요기요에서 크롤링한 식당 정보(배달비/최소주문금액/...) 제공  
    -> 모집 게시글 작성 시간/과정 단축  

### 요기요 자동 주문 접수
    웹 자동화 프레임워크를 통해 서버에서 직접 주문  
    -> 사용자가 주문할 필요 X. 주문과정 간편화  

### "내가 쏜다"
    한 명이 배달비 전액 부담  
    -> 빠른 멤버 모집, 전체주문액 증가에 따른 배달비 감소  

### 포인트 제도
    서비스 내 자체 포인트 제도  
    -> 사용자 간 직접 송금할 필요 X. 안정성  

## 팀원 소개
#### [김윤지](https://github.com/kyunji)
    Back-end 개발, AI 모델 구현, DB 관리  

#### [박윤정](https://github.com/pyj127)
    Back-end 개발, 크롤링/자동화 모델 구현, 서버 관리  

#### [김예원](https://github.com/yeye921)
    Front-end 개발, 외부 API 연동, 채팅 구현    

#### [변하진](https://github.com/ByeonHajin)
    Front-end 개발, AI 모델 구현, 서버 연동    
