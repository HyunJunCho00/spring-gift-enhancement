package gift.repository;

import gift.dto.WishResponseDto;
import gift.entity.Wish;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WishRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    public WishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert=new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("wish")
                .usingGeneratedKeyColumns("id");
    }

    public Wish save(Wish wish) {
        Map<String,Object> params=new HashMap<>();
        params.put("member_id",wish.getMemberId());
        params.put("product_id",wish.getProductId());
        Number key=insert.executeAndReturnKey(params);
        wish.setId(key.longValue());
        return wish;
    }

    public Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT id, member_id, product_id FROM wish WHERE member_id = ? AND product_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql,WISH_ROW_MAPPER, memberId, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<WishResponseDto> findWishesWithProductByMemberId(Long memberId) {
        String sql = "SELECT w.id as wish_id, p.id as product_id, p.name as product_name, p.price as product_price, p.image_url as product_image_url " +
                "FROM wish w " +
                "JOIN products p ON w.product_id = p.id " +
                "WHERE w.member_id = ?";

        RowMapper<WishResponseDto> responseDtoRowMapper = (rs, rowNum) -> new WishResponseDto(
                rs.getLong("wish_id"),
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getInt("product_price"),
                rs.getString("product_image_url")
        );

        return jdbcTemplate.query(sql, responseDtoRowMapper, memberId);
    }

    public int deleteByIdAndMemberId(Long wishId, Long memberId) {
        String sql = "DELETE FROM wish WHERE id = ? AND member_id = ?";
        return jdbcTemplate.update(sql, wishId, memberId);
    }

    private final RowMapper<Wish> WISH_ROW_MAPPER=(rs,rowNum) ->
    new Wish(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id")
    );
}
