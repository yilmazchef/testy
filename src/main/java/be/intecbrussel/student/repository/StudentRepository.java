package be.intecbrussel.student.repository;

import be.intecbrussel.student.data.entity.StudentEntity;
import be.intecbrussel.student.mapper.StudentFullRowMapper;
import be.intecbrussel.student.mapper.StudentViewRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class StudentRepository implements IStudentRepository {

    private final JdbcTemplate jdbc;
    private static final Logger log = LoggerFactory.getLogger(StudentRepository.class);

    @Autowired
    public StudentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Boolean existsByUserName(String username) {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.active = TRUE AND testy_student.active AND testy_user.username = ?";
        log.info(sql);
        final var count = jdbc.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public Boolean existsByUserId(String userId) {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.active = TRUE AND testy_student.active AND testy_user.id = ?";
        log.info(sql);
        final var count = jdbc.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    @Override
    public Integer count() throws SQLException {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.active = TRUE AND testy_student.active";
        final var count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public Integer countByFullName(final String firstName, final String lastName) throws SQLException {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.active = TRUE AND testy_student.active " +
                "AND (testy_student.firstname LIKE ? OR testy_student.lastname LIKE ?)";
        final var count = jdbc.queryForObject(sql, Integer.class, firstName, lastName);
        return count != null ? count : 0;
    }

    @Override
    public Integer countByClassName(final String className) throws SQLException {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.active = TRUE AND testy_student.active " +
                "AND testy_student.classname = ?";
        final var count = jdbc.queryForObject(sql, Integer.class, className);
        return count != null ? count : 0;
    }

    @Override
    public List<StudentEntity> select() throws SQLException {
        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_student.firstname, testy_student.lastname FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.active = TRUE AND testy_student.active";
        return jdbc.query(sql, new StudentFullRowMapper());
    }

    @Override
    public List<StudentEntity> filter(final String filterText) throws SQLException {
        final var sql = "SELECT u.id,\n" +
                "       u.username,\n" +
                "       u.active,\n" +
                "       u.authenticated,\n" +
                "       u.activation,\n" +
                "       u.roles,\n" +
                "       s.firstname,\n" +
                "       s.lastname\n" +
                "FROM testy_user u\n" +
                "         INNER JOIN testy_student s ON u.id = s.userId\n" +
                "WHERE u.active = TRUE\n" +
                "  AND s.active\n" +
                "  AND CONCAT_WS(' ', UPPER(u.username), UPPER(s.firstname), UPPER(s.lastname), UPPER(s.classname))\n" +
                "     LIKE ? \n";

        final var searchString = "%" + filterText.toUpperCase(Locale.ROOT) + "%";

        return jdbc.query(sql, new StudentFullRowMapper(), searchString);
    }

    @Override
    public List<StudentEntity> filter(final String firstName, final String lastName, final String className, final String userName) throws SQLException {
        final var sql = "SELECT u.id,\n" +
                "       u.username,\n" +
                "       u.active,\n" +
                "       u.authenticated,\n" +
                "       u.activation,\n" +
                "       u.roles,\n" +
                "       s.firstname,\n" +
                "       s.lastname\n" +
                "FROM testy_user u\n" +
                "         INNER JOIN testy_student s ON u.id = s.userId\n" +
                "WHERE u.active = TRUE\n" +
                "  AND s.active\n" +
                "  AND UPPER(s.firstname) LIKE ? \n" +
                "  AND UPPER(s.lastname) LIKE ? \n" +
                "  AND UPPER(s.classname) LIKE ? \n" +
                "  AND UPPER(u.username) LIKE ? \n" +
                "\n";

        return jdbc.query(sql, new StudentFullRowMapper(),
                "%" + firstName.toUpperCase(Locale.ROOT) + "%",
                "%" + lastName.toUpperCase(Locale.ROOT) + "%",
                "%" + className.toUpperCase(Locale.ROOT) + "%",
                "%" + userName.toUpperCase(Locale.ROOT) + "%"
        );
    }

    @Override
    public List<StudentEntity> selectByFullName(final String firstName, final String lastName) throws SQLException {
        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_student.firstname, testy_student.lastname FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.active = TRUE AND testy_student.active " +
                "AND (testy_student.firstname LIKE ? OR testy_student.lastname LIKE ?)";
        return jdbc.query(sql, new StudentFullRowMapper(), "%" + firstName, "%" + lastName);
    }

    @Override
    public List<StudentEntity> selectByClassName(final String className) throws SQLException {
        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_student.firstname, testy_student.lastname FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.active = TRUE AND testy_student.active " +
                "AND testy_student.classname = ?";
        return jdbc.query(sql, new StudentFullRowMapper(), className);
    }

    @Override
    public Optional<StudentEntity> selectByUserName(String username) throws SQLException {

        if (!existsByUserName(username)) {
            return Optional.empty();
        }

        final var sql = "SELECT " +
                "testy_user.id, testy_user.authenticated, testy_user.username, testy_user.roles, " +
                "testy_student.firstname, testy_student.lastname FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.username = ? AND testy_user.active = TRUE AND testy_student.active";
        log.info(sql);
        return Optional.ofNullable(jdbc.queryForObject(sql, new StudentViewRowMapper(), username));
    }

    @Override
    public Optional<StudentEntity> selectById(String id) throws SQLException {

        if (!existsByUserId(id)) {
            return Optional.empty();
        }

        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_student.firstname, testy_student.lastname FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.id = ? AND testy_user.active = TRUE AND testy_student.active";
        log.info(sql);
        return Optional.ofNullable(jdbc.queryForObject(sql, new StudentFullRowMapper(), id));
    }

    @Override
    public String save(StudentEntity student) throws SQLException {

        if (student.getId() == null) student.setId(UUID.randomUUID().toString());
        student.setRoles("ANON_ROLE,STUDENT_ROLE");

        final var sql = "INSERT INTO testy_student (id, firstName, lastName, className, userId) VALUES (?, ?, ?, ?, ?)";
        log.info(sql);
        int insertCount = jdbc.update(sql,
                student.getId(), student.getFirstName(), student.getLastName(), student.getClassName(), student.getId());

        if (insertCount <= 0) throw new SQLException("student could not be saved");

        return student.getId();
    }

    @Override
    public String[] save(List<StudentEntity> students) throws SQLException {

        return students.stream().map(student -> {
            String savedStudentId = null;
            try {
                savedStudentId = save(student);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return savedStudentId;
        }).filter(Objects::nonNull).toArray(String[]::new);
    }

    @Override
    public String update(StudentEntity student) throws SQLException {
        final var sql = "UPDATE testy_student SET " +
                "firstName = ?, lastName = ?, className = ?, userId = ?, active = ? " +
                "WHERE id = ?";
        log.info(sql);
        int updateCount = jdbc.update(sql,
                student.getFirstName(), student.getLastName(), student.getClassName(), student.getId(), student.getActive(),
                student.getId());

        if (updateCount <= 0) throw new SQLException("student could not be updated");

        return student.getId();
    }

    @Override
    public String delete(String id) throws SQLException {

        final var sql = "DELETE FROM testy_student WHERE id = ?";
        int deleteCount = jdbc.update(sql, id);

        if (deleteCount <= 0)
            throw new SQLException("student could not be deleted");

        return id;
    }

    @Override
    public Optional<StudentEntity> selectByLoginDetails(String username, String password) throws SQLException {

        if (!existsByUserName(username)) {
            return Optional.empty();
        }

        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_student.firstname, testy_student.lastname FROM " +
                "testy_user INNER JOIN testy_student ON testy_user.id = testy_student.id " +
                "WHERE testy_user.username = ? AND testy_user.password = ? AND testy_user.active = TRUE AND testy_student.active";
        log.info(sql);
        final var data = jdbc.queryForObject(sql, new StudentFullRowMapper(), username, password);
        return Optional.ofNullable(data);
    }

}
