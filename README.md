# 1. blog 어플리케이션 

### 회원 가입 기능
- 이메일 인증 기능
- 비밀번호는 아이디에 포함 된 내용 사용 불가
- 비밀번호는 영문 소문자 및 대문자, 숫자, 특수 기호가 들어가야함
- 비밀번호는 최소 4자에서 15자

### 로그인 기능 
- 회원가입이 정상대로 진행 됨에 따라 user 생성
- user는 로그인 시 비밀 번호 확인을 받아야함 -> 두개의 비밀번호가 가입 시 비밀 번호랑 같아야함
- 로그인이 성공 되면 jwt 토큰 발급
- header에 토큰 인증이 완료 되면 게시글 및 댓글 작성,수정,삭제가 가능함

### 게시글 기능 
- 로그인 , 토큰 인증 후 게시글 조회,작성,수정,삭제 가능
- 인증된 토큰 기반으로 자신의 게시글만 삭제, 수정 가능함
- 검색 필터 기능 -> 제목, 날짜,카테고리 통한 검색 가능


### 댓글 기능 
- 로그인, 토큰 인증 후 댓글 작성,수정,삭제 가능
- 인증된 토큰 기반으로 자신의 댓글만 삭제,수정 가능함


# 2. user case diagram

![제목 없는 다이어그램 drawio - draw io - Chrome 2024-06-30 오후 4_21_19](https://github.com/gooddle/myblog/assets/128583844/572ba74c-07f5-4a67-8c25-b4dd7f1bfa42)

# 3. 주요 정책
- diagram 순으로 진행되어야 한다.
- 게시글 삭제는 soft delete 방법을 사용한다 -> 게시글이 생성 할 시 false 삭제 될 시 true 값으로 저장
- 삭제 된 게시글 수정 및 조회 불가
- 게시글 삭제 처리 할 시 삭제 시간을 저장한다.
- spring 스케줄을 이용하여 매일 밤 자정 삭제되며 삭제 시간이 자정 기준으로 12시간이 지난 글들을 영구 삭제
- 영구 삭제 전에 삭제된 본인의 글은 복구 가능
- redis 이용 이메일 인증 코드 관리 


# 4. ERD

![myblog – comment 2024-06-30 오후 4_52_13](https://github.com/gooddle/myblog/assets/128583844/fcede526-8ffd-49ee-a0bb-0472f7350f75)


# 5. API 명세서 

| Name          | URI(Resource)                               | Method | Status Code |
|---------------|---------------------------------------------| -------------| -------------|
| 게시글 작성        | /api/v1/feeds                               | POST | 201 |
| 게시글 목록 조회     | /api/v1/feeds                               |  GET | 200 |
| 선택한 할일 카드 조회  | /api/v1/feeds/{feedId}                      | GET | 200 |
| 선택한 할일 카드 수정  | /api/v1/feeds/{feedId}                      | PUT | 200 |
| 선택한 할일 카드 삭제  | /api/v1/feeds/{feedId}                      | DELETE | 204 |
| 삭제된 본인 게시글 복구 | /api/v1/feeds/{feedId}                      | PUT | 200|
| 댓글 작성         | /api/v1/feeds/{feedId}/comments             |  POST | 201 |
| 댓글 수정         | /api/v1/feeds/{feedId}/comments/{commentId} | PUT | 200 |
| 댓글 삭제         | /api/v1/feeds/{feedId}/comments/{commentId} | DELETE | 204 |
| 이메일 인증 코드 발송  | /api/v1/send-email-code                     | POST | 201 |
| 회원가입          | /api/v1/users/signup                        | POST | 201 |
| 로그인           | /api/v1/users/login                         | POST | 201 |


