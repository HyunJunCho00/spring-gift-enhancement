package gift.service;

import gift.dto.*;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    public List<OptionResponseDto> getOptionsByProductId(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다: " + productId));
        return optionRepository.findByProductId(productId).stream()
                .map(OptionResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OptionResponseDto addOptionToProduct(Long productId, OptionRequestDto optionDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다: " + productId));

        Option newOption = new Option(optionDto.getName(), optionDto.getQuantity(), product);
        Option savedOption = optionRepository.save(newOption);
        return OptionResponseDto.from(savedOption);
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
        List<Option> options = dto.getOptions().stream()
                .map(optionDto -> new Option(optionDto.getName(), optionDto.getQuantity(), null))
                .toList();
        options.forEach(product::addOption);
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