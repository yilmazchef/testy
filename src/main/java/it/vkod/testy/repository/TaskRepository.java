package it.vkod.testy.repository;

import it.vkod.testy.data.entity.TaskEntity;
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
public class TaskRepository implements ITaskRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger TASK_LOGGER = LoggerFactory.getLogger(TaskRepository.class);

    @Autowired
    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM testy_task WHERE active = TRUE";
        TASK_LOGGER.info(sql);
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    @Override
    public Long count(String questionId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM testy_task WHERE questionId = ? AND active = TRUE";
        TASK_LOGGER.info(sql);
        Long count = jdbcTemplate.queryForObject(sql, Long.class, questionId);
        return count != null ? count : 0;
    }

    @Override
    public String save(TaskEntity task) throws SQLException {
        String sql = "INSERT INTO testy_task (id, value, orderNo, weight, type, questionId) VALUES (?, ?, ?, ?, ?, ?)";
        TASK_LOGGER.info(sql);

        if (task.getId() == null)
            task.setId(UUID.randomUUID().toString());
        int updateCount = jdbcTemplate.update(sql,
                task.getId(), task.getValue(), task.getOrderNo(), task.getWeight(), task.getType(), task.getQuestionId());

        if (updateCount <= 0)
            throw new SQLException("task could not be saved.");

        return task.getId();
    }

    @Override
    public String[] save(List<TaskEntity> tasks) throws SQLException {
        return tasks.stream().map(task -> {
            try {
                return save(task);
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).toArray(String[]::new);
    }

    @Override
    public String update(TaskEntity task) throws SQLException {
        String sql = "UPDATE testy_task SET (value = ?), (orderNo = ?), (weight = ?), (type = ?), (questionId = ?), (active = ?) WHERE (id = ?)";
        TASK_LOGGER.info(sql);
        int updateCount = jdbcTemplate.update(sql,
                task.getValue(), task.getOrderNo(), task.getWeight(), task.getType(), task.getQuestionId(), task.getActive(), task.getId());

        if (updateCount <= 0) {
            throw new SQLException("task could not be updated");
        }

        return task.getId();
    }

    @Override
    public String delete(String id) throws SQLException {
        String sql = "DELETE testy_task WHERE id = ?";
        TASK_LOGGER.info(sql);
        int deleteCount = jdbcTemplate.update(sql, id);
        if (deleteCount <= 0) {
            throw new SQLException("task could not deleted");
        }

        return id;
    }

    @Override
    public List<TaskEntity> selectAll() throws SQLException {
        String sql = "SELECT * FROM testy_task WHERE active = TRUE";
        TASK_LOGGER.info(sql);
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public List<TaskEntity> selectByQuestionId(String questionId) throws SQLException {
        String sql = "SELECT * FROM testy_task where questionId = ? AND active = TRUE";
        TASK_LOGGER.info(sql);
        return jdbcTemplate.query(sql, rowMapper(), questionId);
    }

    @Override
    public List<TaskEntity> selectByQuestionId(String questionId, String type) throws SQLException {
        String sql = "SELECT * FROM testy_task where questionId = ? AND type = ? AND active = TRUE";
        TASK_LOGGER.info(sql);
        return jdbcTemplate.query(sql, rowMapper(), questionId, type);
    }

    @Override
    public Optional<TaskEntity> selectById(String id) throws SQLException {
        String sql = "SELECT * FROM testy_task WHERE id = ? AND active = TRUE";
        TASK_LOGGER.info(sql);
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper(), id));
    }

    private RowMapper<TaskEntity> rowMapper() {
        return (rs, rowNum) -> new TaskEntity()
                .withId(rs.getString("id"))
                .withActive(rs.getBoolean("active"))
                .withOrderNo(rs.getInt("orderNo"))
                .withType(rs.getString("type"))
                .withValue(rs.getString("value"))
                .withWeight(rs.getDouble("weight"))
                .withQuestionId(rs.getString("questionId"));
    }

}
