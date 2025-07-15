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
