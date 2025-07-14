package gift.controller.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import gift.dto.CreateProductRequestDto;
import gift.entity.Product;
import gift.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        CreateProductRequestDto createDto = new CreateProductRequestDto();
        createDto.setName("테스트용 상품");
        createDto.setPrice(10000);
        createDto.setImageUrl("test.jpg");
        savedProduct = productService.create(createDto);
    }

    @Test
    void createProduct_ValidationFail() throws Exception {
        mockMvc.perform(post("/admin/products")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "카카오 금지어"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/products/new"));
    }

    @Test
    void getProductList() throws Exception {
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/products/list"));
    }

    @Test
    void getProductDetail_Success() throws Exception {
        mockMvc.perform(get("/admin/products/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/products/detail"));
    }

    @Test
    void getProductDetail_NotFound() throws Exception {
        mockMvc.perform(get("/admin/products/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_Success() throws Exception {
        mockMvc.perform(post("/admin/products/" + savedProduct.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/products"));
    }
}