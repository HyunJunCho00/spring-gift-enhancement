# spring-gift-enhancement

## 🎁 주요 구현 내용
1. 데이터 접근 계층 리팩터링 (JdbcTemplate → Spring Data JPA)
기존 JdbcTemplate 기반의 데이터 중심 설계를, 객체지향적인 설계를 지향하는 Spring Data JPA 방식으로 전면 리팩터링했다.

Repository 인터페이스: ProductRepository, MemberRepository, WishRepository를 JpaRepository를 상속받는 인터페이스로 전환하여, 반복적인 CRUD 코드를 제거하고 코드의 가독성과 유지보수성을 크게 향상시켰다.

2. 객체지향적 엔티티 설계 및 연관관계 매핑
객체 참조 설계: 테이블의 외래 키(FK)를 필드로 갖던 방식에서 벗어나, 엔티티 간에 직접 객체 참조(@ManyToOne, @OneToMany)를 사용하도록 설계를 개선했다. 이를 통해 서비스 계층은 ID가 아닌 객체 중심으로 로직을 처리할 수 있게 되었다.

양방향 연관관계: Member와 Wish 사이에 양방향 연관관계를 설정하여, member.getWishes()와 같이 객체 그래프 탐색을 통해 연관된 데이터를 쉽게 조회할 수 있도록 구현했다.

3. 서비스 계층 고도화
   
트랜잭션 최적화: 모든 서비스 클래스에 @Transactional(readOnly = true)를 기본으로 적용하고, 데이터 변경이 필요한 CUD(Create, Update, Delete) 메서드에만 @Transactional을 개별적으로 적용하여 읽기/쓰기 트랜잭션을 명확히 분리하고 조회 성능을 최적화했다.

4. 테스트 전략 수립 및 강화
계층별 테스트 분리: 테스트의 목적에 따라 아래와 같이 전략을 분리했다.

Repository 테스트 (@DataJpaTest): JPA 관련 설정만 로드하여 엔티티 매핑과 연관관계의 정확성을 검증하는 학습 테스트를 추가했다.

Service 테스트 (@SpringBootTest): 실제 DB 연동을 포함한 통합 테스트 환경에서 트랜잭션과 더티 체킹 등 핵심 비즈니스 로직을 검증했다.

테스트 환경 분리: application-test.properties를 도입하여, 실제 실행 환경과 테스트 환경의 데이터베이스 및 JPA 설정을 완벽하게 분리함으로써 테스트의 독립성과 안정성을 확보했다.

## 📂 프로젝트 구조
JPA 리팩터링 이후에도 패키지 구조는 역할과 책임에 따라 명확하게 유지된다. 다만, repository 패키지 내부의 구현체가 클래스에서 인터페이스로 변경되었다.

└── src
├── main
│   └── java
│       └── gift
│           ├── ...
│           ├── entity       // @Entity, @Id, @OneToMany 등 JPA 어노테이션으로 매핑
│           ├── repository   // JpaRepository를 상속받는 인터페이스
│           └── service      // @Transactional, 더티 체킹을 활용한 비즈니스 로직
│
└── test
├── java
│   └── gift
│       ├── repository   // @DataJpaTest를 사용한 레포지토리 테스트
│       └── service      // @SpringBootTest를 사용한 서비스 통합 테스트
└── resources
└── application-test.properties // 테스트 전용 설정 파일


## 🎁 주요 구현 내용

1. 상품 및 위시리스트 페이지네이션 기능 구현
   핵심 기능: 사용자가 상품 목록과 위시리스트를 조회할 때, 모든 데이터를 한 번에 불러오는 대신 페이지 단위로 나누어 볼 수 있도록 페이지네이션 기능을 구현했다.

Spring Data JPA 활용: Pageable 객체를 사용하여 클라이언트로부터 페이지 번호(page), 페이지 크기(size), 정렬 기준(sort)을 파라미터로 받고, Page 객체를 통해 데이터 목록과 함께 전체 페이지 수 등의 메타데이터를 응답하도록 설계했다.

2. 백엔드 계층별 페이지네이션 적용
   Repository: findAll, @Query 등 조회 메서드의 반환 타입을 List에서 Page로 변경하고, Pageable을 파라미터로 받도록 수정했다. 특히 @Query에서는 countQuery를 함께 사용하여 성능을 최적화했다.

Service: Repository로부터 받은 Page<Entity>를 Page<DTO>로 변환하는 로직을 추가하여, 컨트롤러에는 변환된 DTO 페이지만 전달하도록 책임을 분리했다.

Controller: API 엔드포인트가 Pageable 파라미터를 받을 수 있도록 수정하고, @PageableDefault를 사용하여 기본 정렬 순서와 페이지 크기를 지정함으로써 API의 안정성을 높였다.

3. 프론트엔드 UI 구현 및 테스트 강화
   View (Thymeleaf & JavaScript): 컨트롤러로부터 전달받은 Page 객체의 정보(totalPages, number, first, last 등)를 활용하여, 페이지 번호와 '이전/다음' 버튼이 동적으로 생성되는 UI를 구현했다.

통합 테스트: MockMvc를 사용하여 페이지네이션 관련 파라미터(page, size, sort)에 따라 API가 올바르게 동작하고, Page 객체 형식의 JSON을 정확히 반환하는지 검증하는 테스트 코드를 추가했다.

## 📂 프로젝트 구조
페이지네이션 구현 이후에도 패키지 구조는 역할과 책임에 따라 명확하게 유지된다. 다만, 각 계층의 메서드 시그니처가 Pageable과 Page를 사용하도록 변경되었다.

└── src
├── main
│   └── java
│       └── gift
│           ├── controller   // @PageableDefault, Pageable 파라미터 사용
│           ├── service      // Page<Entity> -> Page<DTO> 변환 책임
│           └── repository   // Pageable을 파라미터로 받고 Page를 반환
│
└── test
└── java
└── gift
└── controller   // MockMvc를 사용한 페이지네이션 API 테스트

🎁 주요 구현 내용
1. 상품 옵션 기능 구현 및 도메인 모델 고도화
   객체지향적 엔티티 설계: Option 엔티티를 새로 설계하고 Product와 1:N 연관관계를 매핑했다. 특히, 옵션의 재고를 차감하는 subtractQuantity() 비즈니스 로직을 서비스 계층이 아닌 Option 엔티티 내부에 직접 구현하여, 엔티티가 스스로의 상태와 행위를 책임지는 객체지향적인 도메인 모델로 개선했다.

견고한 비즈니스 규칙 적용:

@Table(uniqueConstraints = ...)를 사용하여 동일 상품 내 옵션 이름이 중복되지 않도록 복합 유니크 키 제약조건을 데이터베이스 레벨에서 설정했다.

@Pattern, @Min, @Max 등 Bean Validation 어노테이션을 활용하여 옵션명과 수량에 대한 제약조건을 코드 레벨에서부터 검증했다.

상품 생성 시 반드시 하나 이상의 옵션을 포함하도록 서비스 로직과 DTO를 수정하여, **"상품에는 항상 하나 이상의 옵션이 있어야 한다"**는 요구사항을 만족시켰다.

API 및 관리자 UI 확장:

GET /api/products/{productId}/options: 특정 상품에 속한 모든 옵션 목록을 조회하는 API를 구현했다.

관리자가 상품 상세 페이지에서 직접 옵션을 확인하고, 새로운 옵션을 추가할 수 있도록 AdminProductController와 detail.html 뷰를 확장했다.

단위/통합 테스트:

@DataJpaTest를 사용하여 Option 엔티티의 subtractQuantity() 비즈니스 로직과 복합 유니크 키 제약조건이 올바르게 동작하는지 단위 테스트로 검증했다.

MockMvc를 사용하여 새로 추가된 옵션 조회 API가 명세에 맞게 동작하는지 통합 테스트를 통해 검증했다.

📂 프로젝트 구조
상품 옵션 기능이 추가되면서, Option 관련 엔티티, DTO, Repository가 새로 추가되었다.

└── src
├── main
│   └── java
│       └── gift
│           ├── ...
│           ├── dto
│           │   ├── OptionRequestDto.java   // 옵션 생성/수정 요청 DTO
│           │   └── OptionResponseDto.java  // 옵션 조회 응답 DTO
│           ├── entity
│           │   ├── Product.java  // Option 과 1:N 연관관계 설정
│           │   └── Option.java   // 옵션 엔티티 (비즈니스 로직 포함)
│           ├── repository
│           │   └── OptionRepository.java
│           └── service      // 상품 생성 시 옵션 필수 포함 로직 추가
│
└── test
└── java
└── gift
└── repository   // @DataJpaTest를 사용한 Option 엔티티 테스트