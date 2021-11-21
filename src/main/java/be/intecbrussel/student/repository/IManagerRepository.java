package be.intecbrussel.student.repository;

import be.intecbrussel.student.data.entity.ManagerEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IManagerRepository {

    Boolean existsByUserName(String username);

    Boolean existsByUserId(String userId);

    Optional<ManagerEntity> selectByUserName(String username) throws SQLException;

    Optional<ManagerEntity> selectById(String id) throws SQLException;

    Integer count() throws SQLException;

    String save(ManagerEntity teacher) throws SQLException;

    String[] save(List<ManagerEntity> teachers) throws SQLException;

    String update(ManagerEntity teacher) throws SQLException;

    String delete(String id) throws SQLException;

    List<ManagerEntity> select() throws SQLException;

    Optional<ManagerEntity> selectByLoginDetails(String username, String password) throws SQLException;

}
