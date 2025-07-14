package gift.service;

import gift.dto.CreateProductRequestDto;
import gift.dto.UpdateProductRequestDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("ID " + id + "에 해당하는 상품을 찾을 수 없습니다."));
    }

    @Transactional
    public Product create(CreateProductRequestDto dto) {
        return repository.save(new Product(null, dto.getName(), dto.getPrice(), dto.getImageUrl()));
    }

    @Transactional
    public Product update(Long id,UpdateProductRequestDto dto) {
        Product productUpdate=getById(id);
        productUpdate.setName(dto.getName());
        productUpdate.setPrice(dto.getPrice());
        productUpdate.setImageUrl(dto.getImageUrl());
        repository.update(id,productUpdate);
        return productUpdate;
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(id);
    }
}
