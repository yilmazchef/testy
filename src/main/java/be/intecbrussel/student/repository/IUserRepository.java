package be.intecbrussel.student.repository;

import be.intecbrussel.student.data.entity.UserEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    Integer count() throws SQLException;

    String save(UserEntity user) throws SQLException;

    String update(UserEntity user) throws SQLException;

    String patchAddRole(String userId, String newRole) throws SQLException;

    String patchRemoveRole(String userId, String newRole) throws SQLException;

    String delete(String id) throws SQLException;

    List<UserEntity> selectAll() throws SQLException;

    Optional<UserEntity> selectForLogin(String username, String password) throws SQLException;

    Optional<UserEntity> selectByUserName(String username) throws SQLException;

    Optional<UserEntity> selectById(String id) throws SQLException;

    boolean existsByUserName(String username);

    boolean existsByUserId(String userId);

}
