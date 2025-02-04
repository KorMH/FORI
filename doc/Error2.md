# SQLIntegrityConstraintViolationException 에러
## 발생 과정

### User 탈퇴를 구현하는 도중 계정 삭제가 진행 되지 않고 오류가 발생

### 문제가 Token에 걸려 있는 외래키 제약 조건(Foreign Key Constraint) 때문에 user 엔티티를 삭제하지 못하는 경우

# 해결방법
## 1.CASCAD 설정 추가 (연관된 엔티티도 함께 삭제 )
## 2.Repository에서 토큰을 먼저 삭제하고 User를 삭제 

## 채택 된 해결 방법
### CASCADE 설정 추가 (연관된 엔티티도 함께 삭제)
### 이유는 user가 삭제 되면 Token 엔티티도 자동으로 삭제, 데이터 정합성을 유지 할 수 있다.
### User 삭제 시 Token 테이블에서 해당 user_id를 참조하는 데이터도 삭제됨.



