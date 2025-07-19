package gift.repository;

import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    Optional<Wish> findByMemberAndProduct(Member member, Product product);

    @Modifying
    int deleteByIdAndMemberId(Long wishId, Long memberId);

    @Query(value = "SELECT new gift.dto.WishResponseDto(w.id, p.id, p.name, p.price, p.imageUrl) " +
            "FROM Wish w JOIN w.product p WHERE w.member.id = :memberId",
            countQuery = "SELECT count(w) FROM Wish w WHERE w.member.id = :memberId")
    Page<WishResponseDto> findWithProductByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
