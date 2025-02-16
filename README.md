
<div align="center">

<!-- logo -->
## 블레이버스 해커톤 프로젝트

</div> 

### 📝 프로젝트 소개

----

```
미용실 예약 애플리케이션
```

### 버전

----

spring boot : 3.4.2  
java : 17

### API 명세서

----
https://www.notion.so/API-19795b4d77d680ac9df6ca256a152be8

### 컨벤션 규칙

----

- Git 커밋 메시지는 `faet`, `docs`, `test`, `refactor`, `fix`, `chore` 를 이용한다.
  - feat: 기능 추가, 개발  
  - docs: 문서 작성  
  - test: 테스트 코드 작성  
  - refactor: 리팩토링 작업
  - fix : 버그 수정
  - chore : 그외
    
- 기본적으로 Java 컨벤션을 준수한다. (https://thalals.tistory.com/325)
- 클린코드 원칙에 맞춰 개발한다.
- else 를 사용하지 않는다.
- depth 는 2를 넘어가지 않는다. ex) for 메서드 내부에 if 가 있으면 depth 가 2 이다.
- 상수는 따로 분리한다.
- DTO 매핑은 Service 계층에서 진행한다.


### 디렉토리 구조

-----

- auth : 인증/인가 관련 로직을 구현한다.
- config : 애플리케이션 전역 설정 Bean 을 등록한다.
- constants : 상수를 관리한다.
- domain : 도메인 관련 로직을 구현한다.
  - controller : Rest API 계층
  - dto : dto 네이밍은 요청(xxxRequest), 응답(xxxResponse) 로 정의한다.
  - entity : 테이블과 매핑되는 클래스를 정의한다.
  - repository : 저장소역할을 담당한다.
  - service : 비지니스 로직을 담당한다.
- exception : 예외 관련 처리를 담당한다.
- infra : redis, s3, fcm 등 외부 라이브러리와 연동한다. 


### 실행 방법

----

(1) 루트 디렉토리에 .env 환경설정 파일을 만든다.  
(2) .env 관련 설정은 다음과 같다. (jwt 관련 key 는 개인적으로 연락바람 sin1768@naver.com)
```
DB_URL=jdbc:mysql://localhost:3306/{테이블 이름}?serverTimezone=Asia/Seoul
DB_USERNAME={사용자 db id}
DB_PASSWORD={사용자 db pw}
SECRET_KEY=...
ACCESS_KEY_EXPIRATION_SECONDS=...
REFRESH_KEY_EXPIRATION_SECONDS= ...
```


### 기능 명세서

----

### 로그인/회원가입 기능 목록
- [ ] Google 소셜로그인을 이용해 로그인한다.
- [ ] Google 계정 유/무 를 판단한 뒤 계정이 있으면 사용자 정보를 조회하고, 계정이 존재하지 않으면 Client 에게 회원가입을 요청한다.

### 사용자 관련 기능
- [ ] 자신의 정보를 조회한다. (마이페이지)
- [ ] 자신의 예약 정보를 조회한다.

### 디자이너 선택/예약 기능
- [ ] 사용자 선택에 기반해 디자이너를 조회한다.
  - [ ] 사용자는 사용자는 `얼굴형, 퍼스널 컬러, 헤어 스타일링` 를 선택한다. 
  - [ ] `프로필 사진, 이름, 전문 분야, 한줄 소개, 지역/헤어샵, 후기 개수` 가 필요하다.
- [ ] 선택한 디자이너의 예약 가능한 날짜, 시간을 조회한다.
  - [ ] 3개월 후 까지의 정보만 조회한다.
  - [ ] 연속된 시간으로 중복 선택이 가능한다.
- [ ] 클라이언트가 선택한 정보`(예약 시간, 날짜)`를 바탕으로 디자이너를 예약한다.

### 환불 기능
- [ ] 사용자에게 금액을 환불한 뒤 결제 상태(결제 완료 -> 환불)를 변경한다.

### 결제 기능
- [ ] `카카오페이`를 이용해 결제를 진행한다.
- [ ] 결제 완료시 결제 내역을 저장한다.
- [ ] 결제 완료시 `구글 미트 링크`를 생성한다. (비대면 선택 시)
- [ ] `카카오페이`를 이용해 계좌이체를 진행한다.

### 후기 기능 
- [ ] 컨설팅이 완료된 시점부터 후기를 작성할 수 있다.
  - [ ] 사진, 후기 내용을 저장한다.
  
