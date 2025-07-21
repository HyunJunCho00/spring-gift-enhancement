package gift.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "wish", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "product_id"})
})
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Wish() {

    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Long memberId) {
        this.member = member;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Long productId) {
        this.product = product;
    }
}