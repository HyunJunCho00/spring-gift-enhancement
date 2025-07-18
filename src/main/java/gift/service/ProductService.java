package gift.service;

import gift.dto.CreateProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateProductRequestDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponseDto::from);
    }

    public ProductResponseDto getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("ID " + id + "에 해당하는 상품을 찾을 수 없습니다."));
        return ProductResponseDto.from(product);
    }

    @Transactional
    public ProductResponseDto create(CreateProductRequestDto dto) {
        Product product = new Product(dto.getName(), dto.getPrice(), dto.getImageUrl());
        Product savedProduct = productRepository.save(product);
        return ProductResponseDto.from(savedProduct);
    }

    @Transactional
    public ProductResponseDto update(Long id, UpdateProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("ID " + id + "에 해당하는 상품을 찾을 수 없습니다."));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        return ProductResponseDto.from(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}