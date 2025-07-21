package gift.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "product_options", uniqueConstraints = {
        @UniqueConstraint(
                name = "UK_OPTION_PRODUCT_ID_NAME",
                columnNames = {"product_id", "name"}
        )
})
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 50)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$", message = "옵션명에 허용되지 않는 특수문자가 포함되어 있습니다.")
    private String name;

    @NotNull
    @Column(nullable = false)
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99_999_999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    void setProduct(Product product) {
        this.product = product;
    }

    protected Option() {
    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void subtractQuantity(int amount) {
        int restQuantity = this.quantity - amount;
        if (restQuantity < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quantity = restQuantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}