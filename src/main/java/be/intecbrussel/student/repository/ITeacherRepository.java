package be.intecbrussel.student.repository;

import be.intecbrussel.student.data.entity.TeacherEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ITeacherRepository {

    Boolean existsByUserName(String username);

    Boolean existsByUserId(String userId);

    Optional<TeacherEntity> selectByUserName(String username) throws SQLException;

    Optional<TeacherEntity> selectById(String id) throws SQLException;

    Integer count() throws SQLException;

    String save(TeacherEntity teacher) throws SQLException;

    String[] saveAll(List<TeacherEntity> teachers) throws SQLException;

    String update(TeacherEntity teacher) throws SQLException;

    String delete(String id) throws SQLException;

    List<TeacherEntity> selectAll() throws SQLException;

    Optional<TeacherEntity> selectByLoginDetails(String username, String password) throws SQLException;

}
