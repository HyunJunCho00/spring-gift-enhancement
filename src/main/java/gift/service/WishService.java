package gift.service;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
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
@Transactional(readOnly = true)
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Wish addWish(Member member, WishRequestDto wishRequestDto) {
        Product product = productRepository.findById(wishRequestDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));

        wishRepository.findByMemberAndProduct(member, product).ifPresent(w -> {
            throw new WishAlreadyExistsException("이미 위시리스트에 추가된 상품입니다.");
        });

        Wish wish = new Wish(member, product);
        return wishRepository.save(wish);
    }

    public List<WishResponseDto> getWishesByMember(Member member) {
        return wishRepository.findWithProductByMember_Id(member.getId());
    }

    @Transactional
    public void deleteWish(Long wishId, Member member) {
        int deletedCount = wishRepository.deleteByIdAndMember_Id(wishId, member.getId());
        if (deletedCount == 0) {
            throw new UnauthorizedWishAccessException("삭제 권한이 없거나 존재하지 않는 위시리스트 항목입니다.");
        }
    }
}