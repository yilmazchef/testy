package it.vkod.testy.repository;

import it.vkod.testy.data.entity.QuestionEntity;
import it.vkod.testy.data.entity.TaskEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IQuestionRepository {

    Long count();

    String save(QuestionEntity question) throws SQLException;

    String save(QuestionEntity question, List<TaskEntity> tasks) throws SQLException;

    String update(QuestionEntity question) throws SQLException;

    String delete(String id) throws SQLException;

    List<QuestionEntity> select() throws SQLException;

    Optional<QuestionEntity> select(String id) throws SQLException;

}
