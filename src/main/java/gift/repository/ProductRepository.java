package gift.repository;

import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("products");
    }

    public Product save(Product product) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", product.getName());
        map.put("price", product.getPrice());
        map.put("image_url", product.getImageUrl());
        Number key = insert.executeAndReturnKey(new MapSqlParameterSource(map));
        product.setId(key.longValue());
        return product;
    }

    public List<Product> findAll() {
        String sql = "select * from products";
        return jdbcTemplate.query(sql, PRODUCT_ROW_MAPPER);
    }

    public Optional<Product> findById(Long id) {
        String sql = "select * from products where id=?";
        try {
            Product product= jdbcTemplate.queryForObject(sql,PRODUCT_ROW_MAPPER,id);
            return Optional.of(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Long id, Product product) {
        String sql = "update products set name = ?, price = ?, image_url = ? where id = ?";
        int affectedRows = jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), id);
        if (affectedRows == 0) {
            throw new ProductNotFoundException("ID" + id + "에 해당하는 상품을 찾을 수 없습니다.");
        }
    }

    public void delete(Long id) {
        String sql = "delete from products where id = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        if (affectedRows == 0) {
            throw new ProductNotFoundException("ID " + id + "에 해당하는 상품을 찾을 수 없어 삭제에 실패했습니다.");
        }
    }

    private static final RowMapper<Product> PRODUCT_ROW_MAPPER = (rs, rowNum) -> new Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url")
    );
}



