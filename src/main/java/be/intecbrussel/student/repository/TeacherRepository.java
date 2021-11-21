package be.intecbrussel.student.repository;

import be.intecbrussel.student.data.entity.TeacherEntity;
import be.intecbrussel.student.mapper.TeacherFullRowMapper;
import be.intecbrussel.student.mapper.TeacherViewRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TeacherRepository implements ITeacherRepository {

    private final JdbcTemplate jdbc;

    private static final Logger log = LoggerFactory.getLogger(TeacherRepository.class);

    @Autowired
    public TeacherRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.jdbc.setSkipUndeclaredResults(true);
    }

    @Override
    public Boolean existsByUserName(String username) {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_teacher ON testy_user.id = testy_teacher.id " +
                "WHERE testy_user.username = ? AND testy_user.active = TRUE AND testy_teacher.active";
        log.info(sql);
        final var count = jdbc.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public Boolean existsByUserId(String userId) {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_teacher ON testy_user.id = testy_teacher.id " +
                "WHERE testy_user.id = ? AND testy_user.active = TRUE AND testy_teacher.active";
        log.info(sql);
        final var count = jdbc.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    @Override
    public Optional<TeacherEntity> selectByUserName(String username) throws SQLException {

        if (existsByUserName(username).equals(Boolean.FALSE)) {
            return Optional.empty();
        }

        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_teacher.firstname, testy_teacher.lastname FROM " +
                "testy_user INNER JOIN testy_teacher ON testy_user.id = testy_teacher.id " +
                "WHERE testy_user.username = ? AND testy_user.active = TRUE AND testy_teacher.active";
        log.info(sql);
        final var data = jdbc.queryForObject(sql, new TeacherViewRowMapper(), username);
        return Optional.ofNullable(data);
    }

    @Override
    public Optional<TeacherEntity> selectById(String id) throws SQLException {

        if (existsByUserId(id).equals(Boolean.FALSE)) {
            return Optional.empty();
        }

        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_teacher.firstname, testy_teacher.lastname FROM " +
                "testy_user INNER JOIN testy_teacher ON testy_user.id = testy_teacher.id " +
                "WHERE testy_user.id = ? AND testy_user.active = TRUE AND testy_teacher.active";
        log.info(sql);
        final var data = jdbc.queryForObject(sql, new TeacherFullRowMapper(), id);
        return Optional.ofNullable(data);
    }

    @Override
    public Integer count() throws SQLException {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_teacher ON testy_user.id = testy_teacher.id " +
                "WHERE testy_user.active = TRUE AND testy_teacher.active";
        final var count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public String save(TeacherEntity teacher) throws SQLException {

        if (teacher.getId() == null) teacher.setId(UUID.randomUUID().toString());
        teacher.setRoles("ANON_ROLE,STUDENT_ROLE,TEACHER_ROLE");

        final var sql = "INSERT INTO testy_teacher (id, firstName, lastName, userId) VALUES (?, ?, ?, ?)";
        log.info(sql);
        int insertCount = jdbc.update(sql,
                teacher.getId(), teacher.getFirstName(), teacher.getLastName(), teacher.getId());

        if (insertCount <= 0)
            throw new SQLException("teacher could not be saved");

        return teacher.getId();
    }

    @Override
    public String[] saveAll(List<TeacherEntity> teachers) throws SQLException {

        return teachers.stream().map(teacher -> {
            String savedTeacherId = null;
            try {
                savedTeacherId = save(teacher);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return savedTeacherId;
        }).filter(Objects::nonNull).toArray(String[]::new);
    }

    @Override
    public String update(TeacherEntity teacher) throws SQLException {
        final var sql = "UPDATE testy_teacher SET " +
                "firstName = ?, lastName = ?, userId = ?, active = ? WHERE id = ?";
        int updateCount = jdbc.update(sql, teacher.getFirstName(), teacher.getLastName(), teacher.getActive(), teacher.getId());

        if (updateCount <= 0)
            throw new SQLException("teacher could not be updated");

        return teacher.getId();
    }

    @Override
    public String delete(String id) throws SQLException {

        final var sql = "DELETE FROM testy_teacher WHERE id = ?";
        int deleteCount = jdbc.update(sql, id);

        if (deleteCount <= 0)
            throw new SQLException("teacher could not be deleted");

        return id;
    }

    @Override
    public List<TeacherEntity> selectAll() throws SQLException {

        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_teacher.firstname, testy_teacher.lastname FROM " +
                "testy_user INNER JOIN testy_teacher ON testy_user.id = testy_teacher.id " +
                "WHERE testy_user.active = TRUE AND testy_teacher.active";
        return jdbc.query(sql, new TeacherFullRowMapper());
    }

    @Override
    public Optional<TeacherEntity> selectByLoginDetails(String username, String password) throws SQLException {

        if (existsByUserName(username).equals(Boolean.FALSE)) {
            return Optional.empty();
        }

        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_teacher.firstname, testy_teacher.lastname FROM " +
                "testy_user INNER JOIN testy_teacher ON testy_user.id = testy_teacher.id " +
                "WHERE testy_user.username = ? AND testy_user.password = ? AND testy_user.active = TRUE AND testy_teacher.active";
        log.info(sql);
        return Optional.ofNullable(jdbc.queryForObject(sql, new TeacherFullRowMapper(), username, password));
    }

}
