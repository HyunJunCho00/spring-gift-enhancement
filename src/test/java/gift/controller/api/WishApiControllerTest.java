package gift.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.WishRequestDto;
import gift.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/test-data.sql")
public class WishApiControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String userAToken;

    @BeforeEach
    void setUp() {
        this.userAToken = jwtTokenProvider.createToken(1L);
    }

    @Test
    void addWishSuccess() throws Exception {
        WishRequestDto wishRequest = new WishRequestDto();
        wishRequest.setProductId(102L);
        mockMvc.perform(post("/api/wishes")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wishRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void addWishFailWhenDuplicate() throws Exception {
        WishRequestDto wishRequest = new WishRequestDto();
        wishRequest.setProductId(101L);

        mockMvc.perform(post("/api/wishes")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wishRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void getWishesSuccess() throws Exception {
        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(101L));
    }

    @Test
    void deleteWishSuccess() throws Exception {
        Long wishIdToDelete = 1001L;

        mockMvc.perform(delete("/api/wishes/" + wishIdToDelete)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWishFailWhenUnauthorized() throws Exception {
        String userBToken = jwtTokenProvider.createToken(2L);
        Long wishIdOfUserA = 1001L;
        mockMvc.perform(delete("/api/wishes/" + wishIdOfUserA)
                        .header("Authorization", "Bearer " + userBToken))
                .andExpect(status().isForbidden());
    }
}