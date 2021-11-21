package be.intecbrussel.student.repository;

import be.intecbrussel.student.data.entity.StudentEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IStudentRepository {

    Boolean existsByUserName(String username);

    Boolean existsByUserId(String userId);

    Integer count() throws SQLException;

    Integer countByFullName(final String firstName, final String lastName) throws SQLException;

    Integer countByClassName(final String className) throws SQLException;

    List<StudentEntity> filter(final String filterText) throws SQLException;

    List<StudentEntity> filter(final String firstName, final String lastName, final String className, final String userName) throws SQLException;

    List<StudentEntity> select() throws SQLException;

    List<StudentEntity> selectByFullName(final String firstName, final String lastName) throws SQLException;

    List<StudentEntity> selectByClassName(final String classCode) throws SQLException;

    Optional<StudentEntity> selectByUserName(String username) throws SQLException;

    Optional<StudentEntity> selectById(String id) throws SQLException;

    String save(StudentEntity book) throws SQLException;

    String[] save(List<StudentEntity> exams) throws SQLException;

    String update(StudentEntity book) throws SQLException;

    String delete(String id) throws SQLException;

    Optional<StudentEntity> selectByLoginDetails(String username, String password) throws SQLException;

}
