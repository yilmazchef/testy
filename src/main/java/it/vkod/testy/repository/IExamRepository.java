package it.vkod.testy.repository;

import it.vkod.testy.data.entity.ExamEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IExamRepository {

    Long count() throws SQLException;

    Long countByQuestions(final Set<String> questionIdSet, final String session) throws SQLException;

    Boolean exists(final ExamEntity exam) throws SQLException;

    Boolean exists(final String examId) throws SQLException;

    Boolean exists(final String session, final String studentId, final String questionId) throws SQLException;

    Boolean existsByTask(final String taskId, final String session) throws SQLException;

    Boolean existsByCode(final String code) throws SQLException;

    String save(final ExamEntity exam) throws SQLException;

    String update(final ExamEntity exam) throws SQLException;

    String patch(final String examId, final Boolean isSelected) throws SQLException;

    String patchTask(final String taskId, final String session, final Boolean isSelected) throws SQLException;

    List<String> patchSession(final String examCode, final String session) throws SQLException;

    Set<String> patchTasks(final Set<String> taskIdSet, final String session, final Boolean isSelected) throws SQLException;

    String delete(final String id) throws SQLException;

    List<ExamEntity> selectAll() throws SQLException;

    List<ExamEntity> selectAllByCode(final String examCode) throws SQLException;

    List<ExamEntity> selectAllBySession(final String session) throws SQLException;

    List<ExamEntity> selectAllByCodeAndSession(final String code, final String session) throws SQLException;

    Optional<ExamEntity> select(final String id) throws SQLException;
}
