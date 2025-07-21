package gift.controller.admin;


import gift.dto.*;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProductList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<ProductResponseDto> productPage = productService.getAllProducts(pageable);
        model.addAttribute("productPage", productPage);
        return "admin/products/list";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        ProductResponseDto productDto = productService.getById(id);
        List<OptionResponseDto> options = productService.getOptionsByProductId(id);
        model.addAttribute("product", productDto);
        model.addAttribute("options", options);
        model.addAttribute("newOption", new OptionRequestDto());
        return "admin/products/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new CreateProductRequestDto());
        return "admin/products/new";
    }

    @PostMapping("/{productId}/options")
    public String addOption(
            @PathVariable("productId") Long productId,
            @Valid @ModelAttribute("newOption") OptionRequestDto optionDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            ProductResponseDto productDto = productService.getById(productId);
            List<OptionResponseDto> options = productService.getOptionsByProductId(productId);
            model.addAttribute("product", productDto);
            model.addAttribute("options", options);
            return "admin/products/detail";
        }

        productService.addOptionToProduct(productId, optionDto);
        return "redirect:/admin/products/" + productId;
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("product") CreateProductRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/products/new";
        }
        productService.create(dto);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProductResponseDto productDto = productService.getById(id);
        UpdateProductRequestDto updateDto = new UpdateProductRequestDto();
        updateDto.setName(productDto.getName());
        updateDto.setPrice(productDto.getPrice());
        updateDto.setImageUrl(productDto.getImageUrl());

        model.addAttribute("product", updateDto);
        model.addAttribute("productId", id);
        return "admin/products/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("product") UpdateProductRequestDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "admin/products/edit";
        }
        productService.update(id, dto);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}
