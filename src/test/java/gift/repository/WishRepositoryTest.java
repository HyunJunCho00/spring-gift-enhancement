package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WishRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveWishWithRelations() {
        Member member = entityManager.persist(new Member("test@example.com", "password"));
        Product product = entityManager.persist(new Product("테스트 상품", 10000, "test.jpg"));
        Wish wish = new Wish(member, product);
        wishRepository.save(wish);
        entityManager.flush();
        entityManager.clear();
        Wish foundWish = wishRepository.findById(wish.getId()).orElseThrow();

        assertThat(foundWish.getMember().getId()).isEqualTo(member.getId());
        assertThat(foundWish.getProduct().getId()).isEqualTo(product.getId());
        assertThat(foundWish.getMember().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findWishesFromMember() {
        Member member = new Member("unique@example.com", "password");
        Product product1 = productRepository.save(new Product("상품1", 1000, "1.jpg"));
        Product product2 = productRepository.save(new Product("상품2", 2000, "2.jpg"));

        member.getWishes().add(new Wish(member, product1));
        member.getWishes().add(new Wish(member, product2));
        memberRepository.save(member);

        entityManager.flush();
        entityManager.clear();

        Member foundMember = memberRepository.findById(member.getId()).orElseThrow();

        assertThat(foundMember.getWishes()).hasSize(2);
        assertThat(foundMember.getWishes().get(0).getProduct().getName()).isEqualTo("상품1");
    }
}