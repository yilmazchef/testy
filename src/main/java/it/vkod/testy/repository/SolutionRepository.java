package it.vkod.testy.repository;

import it.vkod.testy.data.entity.SolutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SolutionRepository implements ISolutionRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger SOLUTION_LOGGER = LoggerFactory.getLogger(SolutionRepository.class);

    @Autowired
    public SolutionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM testy_solution WHERE active = TRUE";
        SOLUTION_LOGGER.info(sql);
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    @Override
    public Boolean exists(SolutionEntity solution) {
        String sql = "SELECT COUNT(*) FROM testy_solution WHERE (examId = ?) AND (teacherId = ?) AND (content LIKE %?) AND active = TRUE ";
        SOLUTION_LOGGER.info(sql);
        Long count = jdbcTemplate.queryForObject(sql, Long.class,
                solution.getExamId(), solution.getTeacherId(), solution.getContent()
        );
        return count > 0;
    }

    @Override
    public Boolean exists(String session, String studentId, String questionId) {
        String sql = "SELECT COUNT(*) FROM testy_solution WHERE active = TRUE AND (testy_solution.session = ?) AND (testy_solution.studentId = ?) AND (testy_solution.taskId IN (SELECT testy_task.id FROM testy_task WHERE testy_task.questionId = ?) ) ";
        SOLUTION_LOGGER.info(sql);
        Long count = jdbcTemplate.queryForObject(sql, Long.class, session, studentId, questionId);
        return count > 0;
    }

    @Override
    public String save(SolutionEntity solution) throws SQLException {

        String sql = "INSERT INTO testy_solution " +
                "(id, active, examId, teacherId, content, likes, dislikes)" +
                " " + "VALUES" + " " +
                "(?, ?, ?, ?, ?, ?, ?)";
        SOLUTION_LOGGER.info(sql);

        if (exists(solution).equals(Boolean.TRUE)) throw new SQLException("Solution already exists.");

        if (solution.getId() == null) solution.setId(UUID.randomUUID().toString());
        int updateCount = jdbcTemplate.update(sql,
                solution.getId(), solution.getActive(), solution.getExamId(), solution.getTeacherId(), solution.getContent(), solution.getLikes(), solution.getDisLikes()
        );

        if (updateCount <= 0) throw new SQLException("Solution could not be saved.");

        return solution.getId();
    }

    @Override
    public String update(SolutionEntity solution) throws SQLException {
        String sql = "UPDATE testy_solution" +
                " SET " +
                "(active = ?), (examId = ?), (teacherId = ?), (content = ?), (likes = ?), (dislikes = ?)" +
                " WHERE " +
                "(id = ?)";
        SOLUTION_LOGGER.info(sql);
        int updateCount = jdbcTemplate.update(sql,
                solution.getActive(), solution.getExamId(), solution.getTeacherId(), solution.getContent(), solution.getLikes(), solution.getDisLikes(),
                solution.getId()
        );

        if (updateCount <= 0)
            throw new SQLException("solution could not be updated");

        return solution.getId();
    }

    @Override
    public String delete(String id) throws SQLException {
        String sql = "DELETE testy_solution WHERE (id = ?)";
        SOLUTION_LOGGER.info(sql);
        int deleteCount = jdbcTemplate.update(sql, id);
        if (deleteCount <= 0) {
            throw new SQLException("solution could not deleted");
        }

        return id;
    }

    @Override
    public List<SolutionEntity> selectAll() throws SQLException {
        String sql = "SELECT * FROM testy_solution WHERE active = TRUE";
        SOLUTION_LOGGER.info(sql);
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public List<SolutionEntity> selectAll(String session) throws SQLException {
        String sql = "SELECT * FROM testy_solution WHERE session = ? AND active = TRUE";
        SOLUTION_LOGGER.info(sql);
        return jdbcTemplate.query(sql, rowMapper(), session);
    }

    @Override
    public Optional<SolutionEntity> select(String id) throws SQLException {
        String sql = "SELECT * FROM testy_solution WHERE id = ? AND active = TRUE";
        SOLUTION_LOGGER.info(sql);
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper(), id));
    }

    private RowMapper<SolutionEntity> rowMapper() {
        return new BeanPropertyRowMapper<>(SolutionEntity.class, true);
    }

}
