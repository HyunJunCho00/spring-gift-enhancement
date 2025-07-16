package gift.repository;

import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    Optional<Wish> findByMemberAndProduct(Member member, Product product);

    @Query("SELECT new gift.dto.WishResponseDto(w.id, p.id, p.name, p.price, p.imageUrl) " +
            "FROM Wish w JOIN w.product p WHERE w.member.id = :memberId")
    List<WishResponseDto> findWithProductByMember_Id(@Param("memberId") Long memberId);

    @Modifying
    int deleteByIdAndMemberId(Long wishId, Long memberId);
}
