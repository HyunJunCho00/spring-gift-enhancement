package gift.service;

import gift.dto.CreateProductRequestDto;
import gift.dto.OptionRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateProductRequestDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void updateProductWithDirtyChecking() {
        Product originalProduct = productRepository.save(new Product("원본 상품", 10000, "original.jpg"));
        Long productId = originalProduct.getId();

        UpdateProductRequestDto updateRequest = new UpdateProductRequestDto();
        updateRequest.setName("수정된 상품");
        updateRequest.setPrice(15000);
        updateRequest.setImageUrl("updated.jpg");
        productService.update(productId, updateRequest);
        Product updatedProduct = productRepository.findById(productId).orElseThrow();

        assertThat(updatedProduct.getName()).isEqualTo("수정된 상품");
        assertThat(updatedProduct.getPrice()).isEqualTo(15000);
    }

    @Test
    void createProduct_Success() {
        OptionRequestDto optionDto = new OptionRequestDto();
        optionDto.setName("기본 옵션");
        optionDto.setQuantity(100);

        CreateProductRequestDto createRequest = new CreateProductRequestDto();
        createRequest.setName("새로운 상품");
        createRequest.setPrice(20000);
        createRequest.setImageUrl("new.jpg");
        createRequest.setOptions(List.of(optionDto));

        ProductResponseDto savedProductDto = productService.create(createRequest);
        assertThat(savedProductDto.getName()).isEqualTo("새로운 상품");

        Product foundProduct = productRepository.findById(savedProductDto.getId()).orElseThrow();
        assertThat(foundProduct.getOptions()).hasSize(1);
        assertThat(foundProduct.getOptions().get(0).getName()).isEqualTo("기본 옵션");
    }

    @Test
    void deleteNonExistentProduct() {
        Long nonExistentId = 9999L;
        productService.delete(nonExistentId);
        assertThat(productRepository.findById(nonExistentId)).isEmpty();
    }

    @Test
    void getProductById_NotFound() {
        Long nonExistentId = 9999L;
        assertThatThrownBy(() -> productService.getById(nonExistentId))
                .isInstanceOf(ProductNotFoundException.class);
    }

}