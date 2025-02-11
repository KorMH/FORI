# @NoArgsConstructor 중복 선언으로 인해 Restaurant 클래스의 기본 생성자가 중복 정의됨

## @NoArgsConstructor가 @RequiredArgsConstructor 또는 다른 생성자와 충돌
### @NoArgsConstructor에 (access = AccessLevel.PROTECTED) 를 추가 기본생성자를 보호하려 했으나 해결 되지 않음

# 해결방법
### @NoArgsConstructor에를 지우고 Setter를 추가.

