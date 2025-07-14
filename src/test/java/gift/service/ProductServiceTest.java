package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gift.dto.CreateProductRequestDto;
import gift.dto.UpdateProductRequestDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        CreateProductRequestDto createDto = new CreateProductRequestDto();
        createDto.setName("기본 상품");
        createDto.setPrice(15000);
        createDto.setImageUrl("default.jpg");
        savedProduct = productService.create(createDto);
    }

    @Test
    void getProductById_Success() {
        Product foundProduct = productService.getById(savedProduct.getId());
        assertThat(foundProduct.getName()).isEqualTo("기본 상품");
    }

    @Test
    void getProductByNonExistentId() {
        assertThatThrownBy(() -> productService.getById(99999L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void updateProduct_Success() {
        UpdateProductRequestDto updateDto = new UpdateProductRequestDto();
        updateDto.setName("수정된 상품");
        updateDto.setPrice(20000);
        updateDto.setImageUrl("updated.jpg");

        productService.update(savedProduct.getId(), updateDto);

        Product updatedProduct = productService.getById(savedProduct.getId());
        assertThat(updatedProduct.getName()).isEqualTo("수정된 상품");
    }


    @Test
    void getAllProducts() {
        CreateProductRequestDto anotherDto = new CreateProductRequestDto();
        anotherDto.setName("추가 상품");
        anotherDto.setPrice(5000);
        anotherDto.setImageUrl("another.jpg");
        productService.create(anotherDto);
        List<Product> products = productService.getAll();
        assertThat(products).hasSize(4);
    }

    @Test
    void deleteProduct_Success() {
        Long productId = savedProduct.getId();
        productService.delete(productId);
        assertThatThrownBy(() -> productService.getById(productId))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void deleteNonExistentProduct() {
        Long nonExistentId = 99999L;
        assertThatThrownBy(() -> productService.delete(nonExistentId))
                .isInstanceOf(ProductNotFoundException.class);
    }
}