package gift.service;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Wish;
import gift.exception.ProductNotFoundException;
import gift.exception.UnauthorizedWishAccessException;
import gift.exception.WishAlreadyExistsException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Wish addWish(Member member, WishRequestDto wishRequestDto) {
        Long productId = wishRequestDto.getProductId();
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다. ID: " + productId));

        wishRepository.findByMemberIdAndProductId(member.getId(), productId).ifPresent(w -> {
            throw new WishAlreadyExistsException("이미 위시리스트에 추가된 상품입니다.");
        });

        Wish wish= new Wish(null,member.getId(),productId);
        return wishRepository.save(wish);
    }

    @Transactional
    public List<WishResponseDto> getWishesByMember(Member member) {
        return wishRepository.findWishesWithProductByMemberId(member.getId());
    }

    @Transactional
    public void deleteWish(Long wishId, Member member) {
        int affectedRows = wishRepository.deleteByIdAndMemberId(wishId, member.getId());
        if (affectedRows == 0) {
            throw new UnauthorizedWishAccessException("삭제할 수 없는 위시리스트 항목입니다.");
        }
    }
}
