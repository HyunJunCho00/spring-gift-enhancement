package gift.repository;

import gift.entity.Option;
import gift.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class OptionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OptionRepository optionRepository;

    @Test
    void subtractQuantity() {
        Product product = entityManager.persist(new Product("상품", 1000, "img.jpg"));
        Option option = entityManager.persist(new Option("옵션1", 10, product));

        option.subtractQuantity(3);
        entityManager.flush();
        entityManager.clear();

        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        assertThat(updatedOption.getQuantity()).isEqualTo(7);
    }

    @Test
    void subtractQuantity_throwsException_whenStockIsInsufficient() {
        Product product = entityManager.persist(new Product("상품", 1000, "img.jpg"));
        Option option = entityManager.persist(new Option("옵션1", 5, product));

        assertThatThrownBy(() -> option.subtractQuantity(10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }

    @Test
    void save_throwsException_whenOptionNameIsDuplicateInSameProduct() {
        Product product = entityManager.persist(new Product("상품", 1000, "img.jpg"));
        optionRepository.save(new Option("중복 옵션", 10, product));

        Option duplicateOption = new Option("중복 옵션", 20, product);

        assertThatThrownBy(() -> optionRepository.saveAndFlush(duplicateOption))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

