package it.vkod.testy.repository;

import it.vkod.testy.data.entity.TaskEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ITaskRepository {

    Long count() throws SQLException;

    Long count(String questionId) throws SQLException;

    String save(TaskEntity task) throws SQLException;

    String[] save(List<TaskEntity> tasks) throws SQLException;

    String update(TaskEntity task) throws SQLException;

    String delete(String id) throws SQLException;

    List<TaskEntity> selectAll() throws SQLException;

    List<TaskEntity> selectByQuestionId(String questionId) throws SQLException;

    List<TaskEntity> selectByQuestionId(String questionId, String type) throws SQLException;

    Optional<TaskEntity> selectById(String id) throws SQLException;
}
