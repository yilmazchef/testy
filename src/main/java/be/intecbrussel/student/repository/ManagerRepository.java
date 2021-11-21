package be.intecbrussel.student.repository;

import be.intecbrussel.student.data.entity.ManagerEntity;
import be.intecbrussel.student.mapper.ManagerFullRowMapper;
import be.intecbrussel.student.mapper.ManagerViewRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class ManagerRepository implements IManagerRepository {

    private final JdbcTemplate jdbc;

    private static final Logger log = LoggerFactory.getLogger(ManagerRepository.class);

    @Autowired
    public ManagerRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public Boolean existsByUserName(String username) {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_manager ON testy_user.id = testy_manager.id " +
                "WHERE testy_user.username = ? AND testy_user.active = TRUE AND testy_manager.active";
        log.info(sql);
        final var count = jdbc.queryForObject(sql, Integer.class, username.toLowerCase(Locale.ROOT));
        return count != null && count > 0;
    }

    @Override
    public Boolean existsByUserId(String userId) {
        final var sql = "SELECT COUNT(*) " +
                "FROM " +
                "testy_user INNER JOIN testy_manager ON testy_user.id = testy_manager.id " +
                "WHERE testy_user.id = ? AND testy_user.active = TRUE AND testy_manager.active";
        log.info(sql);
        final var count = jdbc.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    @Override
    public Optional<ManagerEntity> selectByUserName(String username) throws SQLException {

        if (existsByUserName(username).equals(Boolean.FALSE)) {
            return Optional.empty();
        }

        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_manager.firstname, testy_manager.lastname FROM " +
                "testy_user INNER JOIN testy_manager ON testy_user.id = testy_manager.id " +
                "WHERE testy_user.username = ? AND testy_user.active = TRUE AND testy_manager.active";
        log.info(sql);

        final var data = jdbc.queryForObject(sql, new ManagerViewRowMapper(), username);
        return Optional.ofNullable(data);
    }

    @Override
    public Optional<ManagerEntity> selectById(String id) throws SQLException {

        if (existsByUserId(id).equals(Boolean.FALSE)) {
            return Optional.empty();
        }

        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_manager.firstname, testy_manager.lastname FROM " +
                "testy_user INNER JOIN testy_manager ON testy_user.id = testy_manager.id " +
                "WHERE testy_user.id = ? AND testy_user.active = TRUE AND testy_manager.active";
        log.info(sql);

        final var data = jdbc.queryForObject(sql, new ManagerFullRowMapper(), id);
        return Optional.ofNullable(data);
    }

    @Override
    public Integer count() throws SQLException {
        final var sql = "SELECT COUNT(*)" +
                "FROM " +
                "testy_user INNER JOIN testy_manager ON testy_user.id = testy_manager.id " +
                "WHERE testy_user.active = TRUE AND testy_manager.active";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public String save(ManagerEntity manager) throws SQLException {

        if (manager.getId() == null) manager.setId(UUID.randomUUID().toString());

        manager.setRoles("ANON_ROLE,STUDENT_ROLE,TEACHER_ROLE,MANAGER_ROLE");

        final var sql = "insert into testy_manager (id, firstName, lastName, userId) values (?, ?, ?, ?)";
        log.info(sql);

        final var insertCount = jdbc.update(sql,
                manager.getId(), manager.getFirstName(), manager.getLastName(), manager.getId());

        if (insertCount <= 0)
            throw new SQLException("manager could not be saved");

        return manager.getId();
    }

    @Override
    public String[] save(List<ManagerEntity> managers) throws SQLException {

        return managers.stream().map(manager -> {
            String savedManagerId = null;
            try {
                savedManagerId = save(manager);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return savedManagerId;
        }).filter(Objects::nonNull).toArray(String[]::new);
    }

    @Override
    public String update(ManagerEntity manager) throws SQLException {

        final var sql = "UPDATE testy_manager SET " +
                "firstName = ?, lastName = ?, userId = ?, active = ? WHERE id = ?";
        final var updateCount = jdbc.update(sql, manager.getFirstName(), manager.getLastName(), manager.getActive(), manager.getId());

        if (updateCount <= 0)
            throw new SQLException("manager could not be updated");

        return manager.getId();
    }

    @Override
    public String delete(String id) throws SQLException {

        final var sql = "DELETE FROM testy_manager WHERE id = ?";
        int deleteCount = jdbc.update(sql, id);

        if (deleteCount <= 0)
            throw new SQLException("manager could not be deleted");

        return id;
    }

    @Override
    public List<ManagerEntity> select() throws SQLException {
        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_manager.firstname, testy_manager.lastname FROM " +
                "testy_user INNER JOIN testy_manager ON testy_user.id = testy_manager.id " +
                "WHERE testy_user.active = TRUE AND testy_manager.active";
        log.info(sql);

        return jdbc.query(sql, new ManagerFullRowMapper());
    }

    @Override
    public Optional<ManagerEntity> selectByLoginDetails(String username, String password) throws SQLException {

        if (existsByUserName(username).equals(Boolean.FALSE)) {
            return Optional.empty();
        }

        final var sql = "SELECT " +
                "testy_user.id, testy_user.username, testy_user.active, testy_user.authenticated, testy_user.activation, testy_user.roles, " +
                "testy_manager.firstname, testy_manager.lastname FROM " +
                "testy_user INNER JOIN testy_manager ON testy_user.id = testy_manager.id " +
                "WHERE testy_user.username = ? AND testy_user.password = ? AND testy_user.active = TRUE AND testy_manager.active";
        log.info(sql);

        final var data = jdbc.queryForObject(sql, new ManagerFullRowMapper(), username, password);
        return Optional.ofNullable(data);
    }
}
