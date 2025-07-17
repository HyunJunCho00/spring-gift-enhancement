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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

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
        member = new Member("test@test.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);

        product = new Product("테스트 상품", 10000, "test.jpg");
        ReflectionTestUtils.setField(product, "id", 100L);

        wishRequestDto = new WishRequestDto();
        wishRequestDto.setProductId(product.getId());
    }

    @Test
    void addWish_success() {
        given(productRepository.findById(any(Long.class))).willReturn(Optional.of(product));
        given(wishRepository.findByMemberAndProduct(member, product)).willReturn(Optional.empty());
        given(wishRepository.save(any(Wish.class))).willReturn(new Wish(member, product));

        wishService.addWish(member, wishRequestDto);

        ArgumentCaptor<Wish> wishCaptor = ArgumentCaptor.forClass(Wish.class);
        verify(wishRepository).save(wishCaptor.capture());

        Wish savedWish = wishCaptor.getValue();
        assertThat(savedWish.getMember()).isEqualTo(member);
        assertThat(savedWish.getProduct()).isEqualTo(product);
    }

    @Test
    void addWish_fail_productNotFound() {
        given(productRepository.findById(any(Long.class))).willReturn(Optional.empty());

        assertThatThrownBy(() -> wishService.addWish(member, wishRequestDto))
                .isInstanceOf(ProductNotFoundException.class);

        verify(wishRepository, never()).save(any(Wish.class));
    }

    @Test
    void addWish_fail_alreadyExists() {
        given(productRepository.findById(any(Long.class))).willReturn(Optional.of(product));
        given(wishRepository.findByMemberAndProduct(member, product))
                .willReturn(Optional.of(new Wish(member, product)));

        assertThatThrownBy(() -> wishService.addWish(member, wishRequestDto))
                .isInstanceOf(WishAlreadyExistsException.class);
    }

    @Test
    void getWishes_success() {
        Pageable pageable = PageRequest.of(0, 5);
        WishResponseDto wishDto = new WishResponseDto(1L, 100L, "테스트 상품", 10000, "test.jpg");
        List<WishResponseDto> content = List.of(wishDto);
        Page<WishResponseDto> wishPage = new PageImpl<>(content, pageable, 1);
        given(wishRepository.findWithProductByMemberId(member.getId(), pageable)).willReturn(wishPage);
        Page<WishResponseDto> resultPage = wishService.getWishesByMember(member, pageable);
        assertThat(resultPage.getTotalElements()).isEqualTo(1);
        assertThat(resultPage.getContent()).hasSize(1);
        assertThat(resultPage.getContent().get(0).productName()).isEqualTo("테스트 상품");
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
        given(wishRepository.deleteByIdAndMemberId(wishId, member.getId())).willReturn(0);

        assertThatThrownBy(() -> wishService.deleteWish(wishId, member))
                .isInstanceOf(UnauthorizedWishAccessException.class);
    }
}