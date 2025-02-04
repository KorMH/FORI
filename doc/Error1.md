# 순환의존성 문제와 해결하는 부분에서 생긴 문제

## SecurityConfig에서 JwtAuthenticationFilter를 @Lazy로 주입
### - JwtAuthenicationFilter가 제대로 빈으로 관리되지 않아, 필터 체인에서 정상적으로 동작하지 않을 가능성이 있음

## JwtAuthenticationFilter가 @Component로 등록됨
### - JwtAuthenticationFilter를 @Component로 등록하면서 자동으로 빈 관리됨.
### - 하지만 SecurityConfig에서 다시 JwtAuthenticationFilter를 생성하려다 보니 중복된 빈 등록 문제가 발생.

##  JwtTokenProvider의 빈 등록 방식이 모호
### - JwtTokenProvider가 별도의 설정 클래스 없이 등록되어 SecurityConfig와 강하게 결합됨.

# 해결 과정
## JwtAuthenticationFilter를 @Component에서 제거
### - 직접 빈으로 등록하여 관리하도록 변경

## JwtTokenProvider의 빈 등록을 @Configuration 클래스로 분리
### - SecurityConfig에서 분리하여 의존성을 줄이고, 역할을 명확히 분리.

## SecurityConfig에서 JwtAuthenticationFilter를 직접 빈으로 등록
### - 생성자 주입 대신, @Bean을 사용하여 직접 빈을 등록하도록 수정.
### - 이제 SecurityConfig가 JwtAuthenticationFilter를 생성자로 직접 받지 않고, 필요할 때만 @Bean을 통해 생성하여 순환 의존성을 방지.