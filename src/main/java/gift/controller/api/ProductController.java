package gift.controller.api;


import gift.dto.*;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> getOptionsByProduct(@PathVariable("productId") Long productId) {
        List<OptionResponseDto> options = productService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<OptionResponseDto> addOptionToProduct(
            @PathVariable("productId") Long productId,
            @Valid @RequestBody OptionRequestDto optionDto
    ) {
        OptionResponseDto savedOption = productService.addOptionToProduct(productId, optionDto);
        URI location = URI.create("/api/products/" + productId + "/options/" + savedOption.id());
        return ResponseEntity.created(location).body(savedOption);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody CreateProductRequestDto dto) {
        ProductResponseDto createdProduct = productService.create(dto);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody UpdateProductRequestDto requestDto) {
        return ResponseEntity.ok(productService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

