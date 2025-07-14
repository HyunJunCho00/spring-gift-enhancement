# spring-gift-wishlist

## 🎁 주요 구현 내용

#### 1. 예외 처리 시스템 구축
- **역할 분리**: 사용자에게 HTML 페이지를 반환하는 관리자(MVC)와 JSON 데이터를 응답하는 API(REST)의 예외 처리를 `MvcExceptionHandler`와 `ApiExceptionHandler`로 분리했다.
- **중앙 관리**: `@ControllerAdvice`를 사용하여 각 컨트롤러 패키지에서 발생하는 예외를 한곳에서 중앙 관리하도록 설계했다.
- **상세 예외 처리**: `ProductNotFoundException`(404), `MethodArgumentTypeMismatchException`(400), `HttpMessageNotReadableException`(400) 등 다양한 예외 상황에 맞는 HTTP 상태 코드와 명확한 에러 메시지를 반환하도록 구현했다.

#### 2. 서버 측 유효성 검사 강화
- **커스텀 Validator**: 상품명에 대한 비즈니스 규칙(글자 수, 특수문자, 금지어)을 검증하기 위해 `@ValidProductName` 커스텀 어노테이션과 `ProductNameValidator`를 구현했다.
- **DTO 적용**: 상품 추가/수정 DTO에 `@Valid`를 적용하여 컨트롤러 단에서 데이터의 유효성을 우선 검증하도록 했다.

#### 3. 코드 리팩토링 및 안정성 확보
- **서비스 및 레포지토리**: 실패 시 `Optional`이나 `boolean`을 반환하는 대신, 명시적인 예외를 발생시키도록 로직을 개선하여 역할과 책임을 명확히 했다.
- **트랜잭션 관리**: 데이터의 상태를 변경하는 모든 서비스 메서드에 `@Transactional`을 적용하여 데이터의 일관성과 무결성을 보장했다.

#### 4. 테스트 코드 구현
- 애플리케이션의 안정성 확보를 위해 주요 로직에 대한 단위 및 통합 테스트를 작성했다.
- **Validator 단위 테스트**: 순수 로직의 모든 경우의 수를 검증했다.
- **Service 통합 테스트**: DB 연동을 포함한 핵심 비즈니스 흐름(CRUD)을 검증했다.
- **Controller 통합 테스트**: `MockMvc`를 사용하여 HTTP 요청/응답 및 예외 처리 동작을 검증했다.


## 📂 프로젝트 구조

주요 패키지 구조는 역할과 책임에 따라 다음과 같이 분리했다.


└── src
├── main
│   ├── java
│   │   └── gift
│   │       ├── controller
│   │       │   ├── admin
│   │       │   │   ├── AdminProductController.java  // 관리자용 MVC 컨트롤러
│   │       │   │   └── MvcExceptionHandler.java     // MVC 전용 예외 처리기
│   │       │   └── api
│   │       │       ├── ProductController.java       // 외부용 REST API 컨트롤러
│   │       │       └── ApiExceptionHandler.java     // API 전용 예외 처리기
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception  // 전역 커스텀 비즈니스 예외
│   │       ├── repository
│   │       ├── service
│   │       └── validation // 커스텀 유효성 검사기
│   └── resources
│       ├── templates
│       │   ├── admin/products
│       │   └── error      // 상태 코드별 에러 페이지
│       └── data.sql
└── test
└── java
└── gift           // 주요 계층별 테스트 코드 (controller, service, validation)

## 🎁 주요 구현 내용

#### 1. 회원 인증 시스템 구축
- **인증 방식**: 상태가 없는(Stateless) 인증을 위해 **JWT(JSON Web Token)**를 도입했다. 로그인 성공 시(`POST /api/members/login`), 서버는 사용자의 ID를 담은 토큰을 생성하여 응답하도록 구현했다.
- **가입 및 발급**: `POST /api/members/register` 엔드포인트를 통해 신규 회원을 등록하고, 등록 즉시 인증 토큰을 발급하도록 설계했다.
- **API 명세**: 회원가입과 로그인을 위한 API 엔드포인트를 `MemberApiController`에 구현하고, 관련 데이터는 DTO(`RegisterRequestDto`, `LoginRequestDto`)를 통해 전달받도록 했다.

#### 2. 인증 기반 접근 제어
- **인터셉터 구현**: `HandlerInterceptor`를 구현하여, 인증이 필요한 요청의 `Authorization` 헤더에 담긴 JWT를 검증하도록 설계했다.
- **토큰 검증**: `security` 패키지 내에 JWT 생성 및 검증 로직을 중앙화하여 관리하고, 인터셉터는 이 로직을 호출하여 토큰의 유효성을 판단하도록 했다.

#### 3. 인증 관련 예외 처리
- **커스텀 예외 정의**: `EmailAlreadyExistsException`(409)과 `InvalidTokenException`(401) 등 인증 과정에서 발생할 수 있는 비즈니스 예외를 명확하게 정의했다.
- **전역 처리**: `ApiExceptionHandler`에서 새로 정의한 인증 관련 예외들을 처리하여, 역할에 맞는 HTTP 상태 코드와 에러 메시지를 클라이언트에게 반환하도록 구현했다.

#### 4. 회원 기능 테스트 코드 구현
- **Service 통합 테스트**: `MemberService`의 회원가입 및 로그인 로직에 대해 단위/통합 테스트를 작성하여 핵심 비즈니스 로직의 정확성을 검증했다.
- **Controller 통합 테스트**: `MockMvc`를 사용하여 `MemberApiController`의 회원가입, 로그인 API가 올바르게 동작하고 예외 상황을 적절히 처리하는지 통합 테스트를 통해 검증했다.
---

## 📂 프로젝트 구조

Step 2에서는 회원(Member), 인증(Security), 인터셉터(Interceptor) 관련 패키지가 추가되었다.

└── src
├── main
│   └── java
│       └── gift
│           ├── config       // WebMvcConfigurer 등 설정 클래스
│           ├── controller
│           │   ├── admin
│           │   ├── api
│           │   │   ├── MemberApiController.java // 회원 API 컨트롤러
│           │   │   └── ProductController.java
│           │   └── MemberPageController.java  // 회원 페이지 컨트롤러
│           ├── dto
│           ├── entity
│           │   ├── Member.java  // 회원 엔티티
│           │   └── Product.java
│           ├── exception    // 커스텀 예외
│           ├── interceptor  // 로그인 인증 인터셉터
│           ├── repository
│           │   └── MemberRepository.java
│           ├── security     // JWT 관련 유틸리티
│           ├── service
│           │   └── MemberService.java
│           └── validation
└── test
└── java
└── gift           // 계층별 테스트 코드


## 🎁 주요 구현 내용

#### 1. 위시리스트 기능 구현
   핵심 기능: 로그인한 사용자가 원하는 상품을 위시리스트에 추가하고, 자신의 목록을 조회하며, 특정 상품을 삭제할 수 있는 기능을 구현했다.

API 설계: 위시리스트 관리를 위해 WishApiController에 RESTful API 엔드포인트를 설계했다.

POST /api/wishes: 위시리스트에 상품 추가 (이미 존재하는 경우 예외 발생)

GET /api/wishes: 현재 로그인된 사용자의 위시리스트 목록 전체 조회

DELETE /api/wishes/{wishId}: 특정 위시 항목 삭제

백엔드 구현: Wish 엔티티, WishRepository, WishService를 구현하여 위시리스트 관련 비즈니스 로직을 처리하도록 했다.

#### 2. 인증된 사용자 정보 처리 개선
   커스텀 어노테이션 도입: 인증된 사용자 정보를 컨트롤러에서 편리하게 사용하기 위해 @LoginMember 커스텀 어노테이션을 도입했다.

Argument Resolver 구현: HandlerMethodArgumentResolver를 구현하여, 로그인 인터셉터가 세션에 저장한 사용자 정보를 @LoginMember 어노테이션이 붙은 파라미터에 자동으로 주입하도록 설계했다. 이를 통해 컨트롤러는 HttpSession에 직접 의존하지 않고 필요한 사용자 정보를 얻을 수 있게 되어 코드의 가독성과 테스트 용이성이 향상되었다.

#### 3. 위시리스트 관련 예외 처리
   커스텀 예외 정의: 위시리스트 기능의 비즈니스 규칙을 명확히 하기 위해 커스텀 예외를 정의했다.

WishAlreadyExistsException (409 Conflict): 이미 위시리스트에 존재하는 상품을 다시 추가하려고 할 때 발생한다.

UnauthorizedWishAccessException (403 Forbidden): 다른 사용자의 위시리스트에 접근하거나 삭제하려고 할 때 발생한다.

전역 처리: ApiExceptionHandler에서 새로 정의한 예외들을 처리하여, 역할에 맞는 HTTP 상태 코드와 에러 메시지를 클라이언트에게 반환하도록 구현했다.

#### 4. 데이터베이스 및 테스트
   스키마 확장: schema.sql에 wishes 테이블 생성 스크립트를 추가했다. 이 테이블은 members와 products 테이블을 참조하는 외래 키를 포함한다.

테스트 코드: WishServiceTest와 WishApiControllerTest를 작성하여 위시리스트 기능의 비즈니스 로직과 API 연동이 올바르게 동작하는지 검증했다.

## 📂 프로젝트 구조
Step 3에서는 위시리스트(Wish) 기능과 이를 지원하는 Argument Resolver 및 커스텀 어노테이션 관련 패키지가 추가되었다.

└── src
└── main
└── java
└── gift
├── annotation   // @LoginMember 커스텀 어노테이션
├── config
├── controller
│   ├── admin
│   └── api
│       ├── MemberApiController.java
│       ├── ProductController.java
│       └── WishApiController.java   // 위시리스트 API 컨트롤러
├── dto
├── entity
│   ├── Member.java
│   ├── Product.java
│   └── Wish.java      // 위시리스트 엔티티
├── exception
├── interceptor
├── repository
│   └── WishRepository.java
├── resolver     // @LoginMember Argument Resolver
├── security
└── service
└── WishService.java