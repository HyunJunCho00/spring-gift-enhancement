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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WishServiceTest {

    @InjectMocks
    private WishService wishService;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ProductRepository productRepository;

    private Member member;
    private Product product;
    private WishRequestDto wishRequestDto;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "test@test.com", "password");
        product = new Product(100L, "테스트 상품", 10000, "test.jpg");
        wishRequestDto = new WishRequestDto();
        wishRequestDto.setProductId(product.getId());
    }

    @Test
    void addWish_success() {
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(wishRepository.findByMemberIdAndProductId(member.getId(), product.getId())).willReturn(Optional.empty());
        wishService.addWish(member, wishRequestDto);
        verify(wishRepository).save(any(Wish.class));
    }

    @Test
    void addWish_fail_productNotFound() {
        given(productRepository.findById(product.getId())).willReturn(Optional.empty());
        assertThatThrownBy(() -> wishService.addWish(member, wishRequestDto))
                .isInstanceOf(ProductNotFoundException.class);
        verify(wishRepository, never()).save(any(Wish.class));
    }

    @Test
    void addWish_fail_alreadyExists() {
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(wishRepository.findByMemberIdAndProductId(member.getId(), product.getId())).willReturn(Optional.of(new Wish(1L, member.getId(), product.getId())));
        assertThatThrownBy(() -> wishService.addWish(member, wishRequestDto))
                .isInstanceOf(WishAlreadyExistsException.class);
    }

    @Test
    void getWishes_success() {
        WishResponseDto wishDto = new WishResponseDto(1L, product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
        given(wishRepository.findWishesWithProductByMemberId(member.getId())).willReturn(List.of(wishDto));
        List<WishResponseDto> result = wishService.getWishesByMember(member);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).productName()).isEqualTo("테스트 상품");
    }

    @Test
    void deleteWish_success() {
        Long wishId = 1L;
        given(wishRepository.deleteByIdAndMemberId(wishId, member.getId())).willReturn(1);
        wishService.deleteWish(wishId, member);
        verify(wishRepository).deleteByIdAndMemberId(wishId, member.getId());
    }

    @Test
    void deleteWish_fail_unauthorized() {
        Long wishId = 1L;
        given(wishRepository.deleteByIdAndMemberId(wishId, member.getId())).willReturn(0); // 삭제된 행이 없음

        assertThatThrownBy(() -> wishService.deleteWish(wishId, member))
                .isInstanceOf(UnauthorizedWishAccessException.class);
    }
}