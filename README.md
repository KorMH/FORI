# FORI (FOOD REVIEW)
## 음식점 리뷰 서비스
- 간단히 음식점과 음식을 리뷰하고, 예약할 수 있는 서비스입니다.
------------------------------------------------------------------------------------------------------
## 프로젝트 기능 및 설계

### 회원가입 기능
 - 사용자는 회원가입을 통해 서비스를 이용할 수 있습니다.
 - 일반 사용자: 이메일(아이디), 비밀번호
 - 점주: 이메일(아이디), 비밀번호, 가게 이름
 - 이메일은 중복될 수 없으며, 고유해야 합니다.
 - 일반 사용자는 기본적으로 USER 권한을 부여받으며, 점주는 OWNER 권한을 가집니다.

### 로그인 기능
 - 회원가입된 사용자는 로그인할 수 있습니다.
 - 로그인 시 회원가입 때 입력한 이메일과 비밀번호를 사용합니다.
 - JWT를 사용해 인증 및 인가를 처리합니다.

### 리뷰 작성 기능
 - 로그인한 사용자는 권한에 관계없이 리뷰를 작성할 수 있습니다.
 - 리뷰 작성 시 입력 정보:
 - 리뷰 제목 (텍스트)
 - 리뷰 내용 (텍스트)
 - 리뷰 이미지 (AWS S3에 업로드)
 - 본인이 작성한 리뷰는 수정 및 삭제할 수 있습니다.
 - 다른 사용자의 리뷰에는 좋아요와 댓글을 남길 수 있습니다.

### 리뷰 목록 조회 기능
 - 로그인 여부에 관계없이 모든 사용자가 리뷰 목록을 조회할 수 있습니다.
 - 기본 정렬 기준은 최신순입니다.
 - 추가 정렬 기준: 좋아요 많은 순, 댓글 많은 순
 - 리뷰 목록 응답 정보:
 - 리뷰 제목
 - 작성일
 - 좋아요 수
 - 댓글 수
 - 페이징 처리가 지원됩니다.

### 특정 리뷰 조회 기능
 - 로그인 여부와 관계없이 특정 리뷰를 상세 조회할 수 있습니다.
 - 응답 정보:
 - 리뷰 제목
 - 리뷰 내용
 - 작성자
 - 작성일
 - 리뷰 이미지
 - 좋아요 수

### 댓글 작성 기능
 - 로그인한 사용자는 리뷰에 댓글을 작성할 수 있습니다.
 - 댓글 작성 시 입력 정보:
 - 댓글 내용 (텍스트)

### 댓글 목록 조회 기능
 - 특정 리뷰의 댓글 목록을 조회할 수 있습니다.
 - 페이징 처리가 지원되며, 최신순으로 정렬됩니다.
 - 댓글 목록 응답 정보:
 - 댓글 내용
 - 작성자
 - 작성일

### 예약 기능
- 사용자는 점주의 음식점을 예약할 수 있습니다.
- 예약 시 입력 정보:
- 예약 날짜
- 예약 시간
- 예약 인원
- 점주는 자신의 매장에 예약된 내역을 조회할 수 있습니다.
- 예약 기능은 점주와 사용자가 1:1 채팅 방식으로 협의할 수도 있습니다.

### 알림 기능
- 사용자는 예약일이 다가올 때 알림을 받을 수 있습니다.
- 사용자의 리뷰에 댓글이 달리거나 좋아요가 추가되면 알림이 전송됩니다.
- 알림은 SSE(Server-Sent Events) 방식으로 구현됩니다.

### 검색 기능
- 사용자는 음식점을 검색할 수 있습니다.
- 매장명
- 주소
- 전화번호
- 업종


## ERD
![음식리뷰서비스](https://github.com/user-attachments/assets/962ba0e0-3eec-4827-a405-15dbc29f7b40)


## Trouble Shooting

## TechStack
JAVA, Spring boot, Spring Security, jwt, JPA, AWS S3, MYSQL, GIT  
