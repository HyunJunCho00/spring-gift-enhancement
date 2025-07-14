package gift.dto;

import gift.entity.Product;

public record WishResponseDto(
        Long wishId,
        Long productId,
        String productName,
        int productPrice,
        String productImageUrl
) {
    public static WishResponseDto of(Long wishId, Product product) {
        return new WishResponseDto(
                wishId,
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}
