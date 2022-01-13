package it.vkod.testy.repository;

import it.vkod.testy.data.entity.CategoryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryRepository implements ICategoryRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger CATEGORY_LOGGER = LoggerFactory.getLogger(CategoryRepository.class);

    @Autowired
    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM testy_category WHERE active = TRUE";
        CATEGORY_LOGGER.info(sql);
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    @Override
    public Long count(String questionId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM testy_category WHERE questionId = ? AND active = TRUE";
        CATEGORY_LOGGER.info(sql);
        Long count = jdbcTemplate.queryForObject(sql, Long.class, questionId);
        return count != null ? count : 0;
    }

    @Override
    public String save(CategoryEntity category) throws SQLException {
        String sql = "INSERT INTO testy_category (id, value, parentId) VALUES (?, ?, ?)";
        CATEGORY_LOGGER.info(sql);
        category.setId(UUID.randomUUID().toString());
        int insertCount = jdbcTemplate.update(sql, category.getId(), category.getValue(), category.getParentId());

        if (insertCount <= 0)
            throw new SQLException("category could not be saved.");

        return category.getId();
    }

    @Override
    public String[] save(List<CategoryEntity> categories) throws SQLException {
        return categories.stream().map(category -> {
            try {
                return save(category);
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).toArray(String[]::new);
    }

    @Override
    public String update(CategoryEntity category) throws SQLException {
        String sql = "UPDATE testy_category SET (value = ?), (parentId = ?), (active = ?) WHERE id = ?";
        CATEGORY_LOGGER.info(sql);
        int updateCount = jdbcTemplate.update(sql, category.getValue(), category.getParentId(), category.getActive(), category.getId());

        if (updateCount <= 0) {
            throw new SQLException("category could not be updated");
        }

        return category.getId();
    }

    @Override
    public String delete(String id) throws SQLException {
        String sql = "DELETE testy_category WHERE id = ?";
        CATEGORY_LOGGER.info(sql);
        int deleteCount = jdbcTemplate.update(sql, id);
        if (deleteCount <= 0) {
            throw new SQLException("category could not deleted");
        }

        return id;
    }

    @Override
    public List<CategoryEntity> selectAll() throws SQLException {
        String sql = "SELECT *  FROM testy_category WHERE active = TRUE";
        CATEGORY_LOGGER.info(sql);
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public List<CategoryEntity> selectByQuestionId(String questionId) throws SQLException {
        String sql = "SELECT *  FROM testy_category WHERE questionId = ? AND active = TRUE";
        CATEGORY_LOGGER.info(sql);
        return jdbcTemplate.query(sql, rowMapper(), questionId);
    }

    @Override
    public Optional<CategoryEntity> selectById(String id) throws SQLException {
        String sql = "SELECT * FROM testy_category WHERE id = ? AND active = TRUE";
        CATEGORY_LOGGER.info(sql);
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper(), id));
    }

    private RowMapper<CategoryEntity> rowMapper() {
        return (rs, rowNum) -> new CategoryEntity()
                .withId(rs.getString("id"))
                .withActive(rs.getBoolean("active"))
                .withValue(rs.getString("value"))
                .withParentId(rs.getString("parentId"));
    }

}
