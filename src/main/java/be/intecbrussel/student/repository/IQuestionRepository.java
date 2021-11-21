package be.intecbrussel.student.repository;

import be.intecbrussel.student.data.entity.QuestionEntity;
import be.intecbrussel.student.data.entity.TaskEntity;

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
